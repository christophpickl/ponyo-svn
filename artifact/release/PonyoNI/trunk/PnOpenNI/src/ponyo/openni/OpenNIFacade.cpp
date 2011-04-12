#include <ponyo/openni/OpenNIFacade.hpp>

namespace pn {

Log* OpenNIFacade::LOG = NEW_LOG();

OpenNIFacade::OpenNIFacade(UserStateCallback userStateCallback, JointDataCallback jointDataCallback) {
	LOG->debug("new OpenNIFacade(userStateCallback, jointDataCallback )");

	this->userManager = new UserManager(userStateCallback, jointDataCallback);
}

OpenNIFacade::~OpenNIFacade() {
	LOG->debug("~OpenNIFacade()");
	delete this->userManager;
	delete this->updateThread;
}

/*public*/ void OpenNIFacade::startRecording(const char* oniFilePath) throw(OpenNiException) {
	LOG->info("startRecording(oniFilePath)");

	LOG->debug("Initializing context ...");
	CHECK_XN(this->context.Init(), "Could not initialize OpenNI context!");

	LOG->debug("Opening oni file recording ...");
	printf("opening: %s\n", oniFilePath);
	CHECK_XN(this->context.OpenFileRecording(oniFilePath), "Could not open *.oni file!");

	this->internalSetup();
}

/*public*/ void OpenNIFacade::startWithXml(const char* configPath) throw(OpenNiException) {
	LOG->info("startWithXml(configPath)");

	LOG->debug("Initializing context ...");
	CHECK_XN(this->context.InitFromXmlFile(configPath), "Could not initialize OpenNI context from XML!");

	this->internalSetup();
}

/*private*/ void OpenNIFacade::internalSetup() throw(OpenNiException) {
	LOG->info("internalSetup()");

	LOG->debug("Creating depth generator ...");
	CHECK_XN(this->depthGenerator.Create(this->context), "Could not create depth generator!");

	// TODO outsource as client configuration option
//	CHECK_XN(xnSetMirror(this->depthGenerator, true), "Setting mirror mode for depth generator failed!");

	// mandatory to set if starting non-recording, otherwise will fail
//	... if starting non-recording => CHECK_XN(this->depthGenerator.SetMapOutputMode(this->mapMode), "set depth mode");

	try {
		this->userManager->init(this->context);
	} catch(UserManagerException& e) {
		throw OpenNiException(e.getMessage(), AT); // TODO add UserManagerException as exception cause
	}

//	LOG->debug("Starting all generators ...");
//	CHECK_XN(this->context.StartGeneratingAll(), "Could not start generators!");

	LOG->debug("Starting depth generator ...");
	CHECK_XN(this->depthGenerator.StartGenerating(), "Could not start depth generator!");

	this->updateThread = new UpdateThread(this->context, this->depthGenerator, this->userManager);
	this->updateThread->start();
}

/*public*/ void OpenNIFacade::destroy() {
	LOG->info("destroy()");

	this->userManager->unregister();
	this->updateThread->stopAndJoin();

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
