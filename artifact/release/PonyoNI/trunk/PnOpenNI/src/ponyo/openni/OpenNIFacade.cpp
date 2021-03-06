#include <ponyo/openni/OpenNIUtils.hpp>
#include <ponyo/openni/OpenNIFacade.hpp>

namespace pn {

Log* OpenNIFacade::LOG = NEW_LOG();

OpenNIFacade::OpenNIFacade() {
	LOG->debug("new OpenNIFacade( )");
//	Exception e("How the hack called the constructor a second time?!", AT);
//	e.printBacktrace();
}

OpenNIFacade::~OpenNIFacade() {
	LOG->debug("~OpenNIFacade()");

	if(this->imageManager != NULL) {
		delete this->imageManager;
	}
	if(this->depthGenerator != NULL) {
		delete this->depthGenerator;
	}
	if(this->userManager != NULL) {
		delete this->userManager;
	}
}

/*public*/ void OpenNIFacade::startRecording(StartOniConfig& config) throw(OpenNiException) {
	LOG->info2("startRecording(config=%s)", config.toCString());

	LOG->debug("Initializing context ...");
	CHECK_XN(this->context.Init(), "Could not initialize OpenNI context!");

	const char* oniRecordingPath = config.getOniRecordingPath();
	printf("Opening oni file recording: %s\n", oniRecordingPath);
	CHECK_XN(this->context.OpenFileRecording(oniRecordingPath), "Could not open *.oni file!");

	this->internalSetup(config);
}

/*public*/ void OpenNIFacade::startWithXml(StartXmlConfig& config) throw(OpenNiException) {
	LOG->info2("startWithXml(config=%s)", config.toCString());

	const char* xmlConfigPath = config.getXmlConfigPath();
	printf("Initializing context from file: %s\n", xmlConfigPath);
	CHECK_XN(OpenNIUtils::safeInitFromXml(this->context, xmlConfigPath), "Could not initialize OpenNI context from XML! Is the device really properly connected?!");

	this->internalSetup(config);
}

/*private*/ void OpenNIFacade::internalSetup(GenericConfig& config) throw(OpenNiException) {
	LOG->debug("internalSetup(config)");

	LOG->trace2("Setting global mirror mode to: %s", boolToString(config.isMirrorModeEnabled()));
	CHECK_XN(this->context.SetGlobalMirror(config.isMirrorModeEnabled() ? TRUE : FALSE), "Setting global mirror mode failed!");

	bool anyGeneratorCreated = false;

	// ----------- IMAGE GENERATOR
	if(config.isImageGeneratorEnabled()) {
		LOG->debug("Creating image generator ...");
		anyGeneratorCreated = true;

		this->imageGenerator = new xn::ImageGenerator();
		CHECK_XN(this->imageGenerator->Create(this->context), "Could not create image generator!");
		XnMapOutputMode imageMode;
		// TODO set image map output mode for non-xml startup
		this->imageGenerator->GetMapOutputMode(imageMode);
		LOG->debug2("imageGenerator mode = FPS: %i, Resolution: %i/%i", imageMode.nFPS, imageMode.nXRes, imageMode.nYRes);

		this->imageManager = new ImagaManager(*this->imageGenerator, imageMode.nXRes, imageMode.nYRes);
		this->imageManager->init();
	} else {
		LOG->trace("Image generator is disabled.");
	}

	// ----------- DEPTH GENERATOR
	if(config.isDepthGeneratorEnabled()) {
		LOG->debug("Creating depth generator ...");
		anyGeneratorCreated = true;

		this->depthGenerator = new xn::DepthGenerator();
		CHECK_XN(this->depthGenerator->Create(this->context), "Could not create depth generator!");

		// TODO mandatory to set if starting non-recording and non-xml, otherwise will fail!!!
	//	CHECK_XN(this->depthGenerator.SetMapOutputMode(configuration.getSomeFooMapMode()), "Setting output mode for depth generator failed!");
		XnMapOutputMode depthMode;
		this->depthGenerator->GetMapOutputMode(depthMode);
		LOG->debug2("depthGenerator mode = FPS: %i, Resolution: %i/%i", depthMode.nFPS, depthMode.nXRes, depthMode.nYRes);

	} else {
		LOG->trace("Depth generator is disabled.");
	}


	// ----------- USER GENERATOR
	if(config.isUserGeneratorEnabled()) {
		anyGeneratorCreated = true;

		// TODO validate configuration upfront (doing it in here would mean user has to wait until preceding work is done...)
		if(config.isDepthGeneratorEnabled() == false) {
			throw Exception("User generator was enabled, but depending depth generator was set to disabled!", AT);
		}

		this->userManager = new UserManager(config.getUserCallback(), config.getJointCallback());
		try {
			this->userManager->init(this->context);
		} catch(UserManagerException& e) {
			throw OpenNiException(e.getMessage(), AT); // MINOR add UserManagerException as exception cause
		}
	}

	printf("Dump of existing nodes:\n=======================\n");
	OpenNIUtils::dumpNodeInfosByContext(this->context);

	if(anyGeneratorCreated == false) {
		LOG->warn("No generators were enabled so you won't receive any data!");
		return;
	}

	LOG->debug("Starting generators ...");
	CHECK_XN(this->context.StartGeneratingAll(), "Could not start generators!");
//	LOG->debug("Starting depth generator ...");
//	CHECK_XN(this->depthGenerator.StartGenerating(), "Could not start depth generator!");

	AsyncExceptionCallback asyncExceptionCallback = config.getAsyncExceptionCallback();
	if(asyncExceptionCallback == NULL) {
		LOG->warn("Installing default async exception callback; you will not be able to handle those :(");
		LOG->warn("Please register to this event via: config.setAsyncExceptionCallback(&myFunction);");
		asyncExceptionCallback = &OpenNIFacade::defaultAsyncExceptionCallback;
	}

	this->updateThread.start(this->context, this, &OpenNIFacade::onUpdateThread, asyncExceptionCallback);

	LOG->info("Starting up OpenNiFacade finished successfully.");
}

/*static*/ void OpenNIFacade::defaultAsyncExceptionCallback(const char* message, Exception& e) throw(Exception) {
	LOG->fatal2("Background throw died: %s\n", message);
	LOG->fatal2("Async exception message: %s\n", e.getMessage());

	fprintf(stderr, "Uncaught async exception: %s!\n", e.getMessage());
	e.printBacktrace();

	throw e; // just kill it ;)
}

void OpenNIFacade::toggleMirror() throw (OpenNiException) {
	LOG->debug("toggleMirror()");
	CHECK_XN(this->context.SetGlobalMirror(!this->context.GetGlobalMirror()), "Toggling mirror mode failed!");
}

void OpenNIFacade::setWindowVisible(bool visible) {
	LOG->debug2("setWindowVisible(visible=%s)", boolToString(visible));

	if(this->imageManager == NULL) {
		LOG->warn("Can not set window visibility as no image manager is available!");
		return;
	}
	this->imageManager->setWindowVisible(visible);
}

bool OpenNIFacade::isWindowVisible() {
	return this->imageManager->isWindowVisible();
}

void OpenNIFacade::onUpdateThread() {

	// FIXME complete rework: dont dispatch single events (joints, etc) but rather dispatch ONE BIG event containing all data => therefore max events (== cpp/java ping-pong) as fps defined!
	if(this->depthGenerator != NULL) {
		this->depthGenerator->GetMetaData(this->depthMetaData); // have to unnecessary read to get updates from user generator :-/

//		TODO xn::UserPositionCapability userPositions = this->depthGenerator->GetUserPositionCap();
//		XnUInt32 userCount = userPositions.GetSupportedUserPositionsCount(); =====> always returns 0 :(
//		printf("GetSupportedUserPositionsCount = %i\n", userCount);
//		XnBoundingBox3D position;
//		for (int i = 0; i < userCount; ++i) {
//			CHECK_XN(userPositions.GetUserPosition(i, position), "Getting user bounding box position failed!");
//			printf("%i. position LeftBottomNear.Z ==> %f\n", i, position.LeftBottomNear.Z);
//		}

	}
	if(this->userManager != NULL) {
		this->userManager->update(); // read and broadcast new skeleton data
	}
	if(this->imageManager != NULL) {
		this->imageManager->update();
	}
}

/*public*/ void OpenNIFacade::shutdown() {
	// FIXME could be the case that this is an async call => while being in startup method... somehow solve!
	LOG->info("shutdown()");

	if(this->imageManager != NULL) {
		this->imageManager->destroy();
	}

	if(this->userManager != NULL) {
		this->userManager->unregister();
	}

	this->updateThread.stopAndJoin();

	// TODO on StopGeneratingAll() getting "The node is locked for changes!" - temporary hack => just shutdown without stopping ;)
//	try {
//		CHECK_XN(this->context.StopGeneratingAll(), "Could not stop generators!");
//	} catch(OpenNiException& e) {
//		LOG->warn("Could not stop user manager!"); // MINOR add exception as log argument
//		e.printBacktrace();
//	}

	LOG->debug("Shutting down OpenNI context...");
	this->context.Shutdown();
}

}
