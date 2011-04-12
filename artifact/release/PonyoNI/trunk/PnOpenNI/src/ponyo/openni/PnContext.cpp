#include <ponyo/openni/PnContext.hpp>

namespace pn {

Log* PnContext::LOG = NEW_LOG();

PnContext::PnContext() {
	LOG->debug("new PnContext()");

	this->userManager = new UserManager();
}

PnContext::~PnContext() {
	LOG->debug("~PnContext()");
	delete this->userManager;
	delete this->updateThread;
}

/*public*/ void PnContext::startRecording(const char* oniFilePath) throw(OpenNiException) {
	LOG->info("startRecording(oniFilePath)");

	LOG->debug("Initializing context ...");
	XNTRY(this->context.Init(), "Could not initialize OpenNI context!");

	LOG->debug("Opening oni file recording ...");
	printf("opening: %s\n", oniFilePath);
	XNTRY(this->context.OpenFileRecording(oniFilePath), "Could not open *.oni file!");

	this->internalSetup();
}

/*public*/ void PnContext::startWithXml(const char* configPath) throw(OpenNiException) {
	LOG->info("startWithXml(configPath)");

	LOG->debug("Initializing context ...");
	XNTRY(this->context.InitFromXmlFile(configPath), "Could not initialize OpenNI context from XML!");

	this->internalSetup();
}

/*private*/ void PnContext::internalSetup() throw(OpenNiException) {
	LOG->info("internalSetup()");

	LOG->debug("Creating depth generator ...");
	XNTRY(this->depthGenerator.Create(this->context), "Could not create depth generator!");

	// TODO outsource as client configuration option
//	XNTRY(xnSetMirror(this->depthGenerator, true), "Setting mirror mode for depth generator failed!");

	// mandatory to set if starting non-recording, otherwise will fail
//	... if starting non-recording => XNTRY(this->depthGenerator.SetMapOutputMode(this->mapMode), "set depth mode");

	try {
		this->userManager->init(this->context);
	} catch(UserManagerException& e) {
		throw OpenNiException(e.getMessage(), AT); // TODO add UserManagerException as exception cause
	}

//	LOG->debug("Starting all generators ...");
//	XNTRY(this->context.StartGeneratingAll(), "Could not start generators!");

	LOG->debug("Starting depth generator ...");
	XNTRY(this->depthGenerator.StartGenerating(), "Could not start depth generator!");

	this->updateThread = new UpdateThread(this->context, this->depthGenerator, this->userManager);
	this->updateThread->start();
}

/*public*/ void PnContext::destroy() {
	LOG->info("destroy()");

	this->userManager->unregister();
	this->updateThread->stopAndJoin();
//	this->depthGenerator

	try {
		XNTRY(this->context.StopGeneratingAll(), "Could not stop generators!");
	} catch(OpenNiException& e) {
		LOG->warn("Could not stop user manager!"); // TODO add exception as log argument
		e.printBacktrace();
	}

	LOG->debug("Shutting down OpenNI context...");
	this->context.Shutdown();
}

}
