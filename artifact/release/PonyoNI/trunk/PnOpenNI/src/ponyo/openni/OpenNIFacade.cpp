#include <ponyo/openni/OpenNIUtils.hpp>
#include <ponyo/openni/OpenNIFacade.hpp>

namespace pn {

Log* OpenNIFacade::LOG = NEW_LOG();

OpenNIFacade::OpenNIFacade() {
	LOG->debug("new OpenNIFacade( )");
}
OpenNIFacade::~OpenNIFacade() {
	LOG->debug("~OpenNIFacade()");

	if(this->userManager != NULL) {
		delete this->userManager;
	}
	if(this->imageManager != NULL) {
		delete this->imageManager;
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
	LOG->info("internalSetup(config)");

	CHECK_XN(this->context.SetGlobalMirror(config.isMirrorModeEnabled() ? TRUE : FALSE),
		"Setting global mirror mode failed!");

	LOG->debug("Creating depth generator ...");
	CHECK_XN(this->depthGenerator.Create(this->context), "Could not create depth generator!");

	// TODO mandatory to set if starting non-recording and non-xml, otherwise will fail!!!
//	CHECK_XN(this->depthGenerator.SetMapOutputMode(configuration.getSomeFooMapMode()), "Setting output mode for depth generator failed!");

	XnMapOutputMode depthMode;
	this->depthGenerator.GetMapOutputMode(depthMode);
	LOG->debug2("depthGenerator mode = FPS: %i, Resolution: %i/%i", depthMode.nFPS, depthMode.nXRes, depthMode.nYRes);

	this->userManager = new UserManager(config.getUserCallback(), config.getJointCallback());
	try {
		this->userManager->init(this->context);
	} catch(UserManagerException& e) {
		throw OpenNiException(e.getMessage(), AT); // MINOR add UserManagerException as exception cause
	}

	if(config.isImageGeneratorEnabled()) {
		LOG->debug("Creating image generator ...");

		CHECK_XN(this->imageGenerator.Create(this->context), "Could not create image generator!");
		XnMapOutputMode imageMode;
		// TODO set image map output mode for non-xml startup
		this->imageGenerator.GetMapOutputMode(imageMode);
		LOG->debug2("imageGenerator mode = FPS: %i, Resolution: %i/%i", imageMode.nFPS, imageMode.nXRes, imageMode.nYRes);

		this->imageManager = new ImagaManager(this->imageGenerator);
		this->imageManager->init();
	}

	LOG->debug("Dump of existing nodes:");
	OpenNIUtils::dumpNodeInfosByContext(this->context);

	LOG->debug("Starting depth generator ...");
	CHECK_XN(this->depthGenerator.StartGenerating(), "Could not start depth generator!");

	this->updateThread.start(this->context, this, &OpenNIFacade::onUpdateThread);
}

void OpenNIFacade::toggleMirror() {
	CHECK_XN(this->context.SetGlobalMirror(!this->context.GetGlobalMirror()), "Toggling mirror mode failed!");
}


void OpenNIFacade::onUpdateThread() {
	this->depthGenerator.GetMetaData(this->depthMetaData); // have to unnecessary read to get updates from user generator :-/
	this->userManager->update(); // read and broadcast new skeleton data
}

/*public*/ void OpenNIFacade::shutdown() {
	// FIXME could be the case that this is an async call => while being in startup method... somehow solve!
	LOG->info("shutdown()");

	if(this->imageManager != NULL) {
		this->imageManager->unregister();
	}
	this->userManager->unregister();

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
