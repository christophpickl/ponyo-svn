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
}


/*public*/ void OpenNIFacade::startRecording(const char* oniFilePath, UserStateCallback userCallback, JointPositionCallback jointCallback) throw(OpenNiException) {
	LOG->info2("startRecording(oniFilePath=%s)", oniFilePath);

	LOG->debug("Initializing context ...");
	CHECK_XN(this->context.Init(), "Could not initialize OpenNI context!");

	LOG->debug("Opening oni file recording ...");
	printf("opening: %s\n", oniFilePath);
	CHECK_XN(this->context.OpenFileRecording(oniFilePath), "Could not open *.oni file!");

	this->internalSetup(userCallback, jointCallback);
}

/*public*/ void OpenNIFacade::startWithXml(const char* configPath, UserStateCallback userCallback, JointPositionCallback jointCallback) throw(OpenNiException) {
	LOG->info2("startWithXml(configPath=%s)", configPath);

	printf("Initializing context from file: %s\n", configPath);
	CHECK_XN(this->context.InitFromXmlFile(configPath), "Could not initialize OpenNI context from XML! Is the device really properly connected?!");

	this->internalSetup(userCallback, jointCallback);
}

/*private*/ void OpenNIFacade::internalSetup(UserStateCallback userCallback, JointPositionCallback jointCallback) throw(OpenNiException) {
	LOG->info("internalSetup()");

	LOG->debug("Creating depth generator ...");
	CHECK_XN(this->depthGenerator.Create(this->context), "Could not create depth generator!");

	// TODO outsource as client configuration option
//	CHECK_XN(xnSetMirror(this->depthGenerator, true), "Setting mirror mode for depth generator failed!");

	// mandatory to set if starting non-recording, otherwise will fail
//	... if starting non-recording => CHECK_XN(this->depthGenerator.SetMapOutputMode(this->mapMode), "set depth mode");

	this->userManager = new UserManager(userCallback, jointCallback);
	try {
		this->userManager->init(this->context);
	} catch(UserManagerException& e) {
		throw OpenNiException(e.getMessage(), AT); // TODO add UserManagerException as exception cause
	}

	OpenNIUtils::dumpNodeInfosByContext(this->context);

	LOG->debug("Starting depth generator ...");
	CHECK_XN(this->depthGenerator.StartGenerating(), "Could not start depth generator!");

	this->updateThread.start(this->context, this, &OpenNIFacade::onUpdateThread);
}

void OpenNIFacade::onUpdateThread() {
	this->depthGenerator.GetMetaData(this->depthMetaData); // have to unnecessary read to get updates from user generator :-/
	this->userManager->update(); // read and broadcast new skeleton data
}

/*public*/ void OpenNIFacade::shutdown() {
	LOG->info("shutdown()");

	this->userManager->unregister();
	this->updateThread.stopAndJoin();

	// FIXME on StopGeneratingAll() getting "The node is locked for changes!" - temporary hack => just shutdown without stopping ;)
//	try {
//		CHECK_XN(this->context.StopGeneratingAll(), "Could not stop generators!");
//	} catch(OpenNiException& e) {
//		LOG->warn("Could not stop user manager!"); // TODO add exception as log argument
//		e.printBacktrace();
//	}

	LOG->debug("Shutting down OpenNI context...");
	this->context.Shutdown();
}

}
