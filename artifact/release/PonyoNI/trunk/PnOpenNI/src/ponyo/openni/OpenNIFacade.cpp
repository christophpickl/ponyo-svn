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


/*public*/ void OpenNIFacade::startRecording(StartOniConfiguration& configuration) throw(OpenNiException) {
	LOG->info2("startRecording(configuration=%s)", configuration.toCString());

	LOG->debug("Initializing context ...");
	CHECK_XN(this->context.Init(), "Could not initialize OpenNI context!");

	const char* oniRecordingPath = configuration.getOniRecordingPath();
	printf("Opening oni file recording: %s\n", oniRecordingPath);
	CHECK_XN(this->context.OpenFileRecording(oniRecordingPath), "Could not open *.oni file!");

	this->internalSetup(configuration);
}

/*public*/ void OpenNIFacade::startWithXml(StartXmlConfiguration& configuration) throw(OpenNiException) {
	LOG->info2("startWithXml(configuration=%s)", configuration.toCString());

	const char* xmlConfigPath = configuration.getXmlConfigPath();
	printf("Initializing context from file: %s\n", xmlConfigPath);
	CHECK_XN(this->context.InitFromXmlFile(xmlConfigPath), "Could not initialize OpenNI context from XML! Is the device really properly connected?!");

	this->internalSetup(configuration);
}

/*private*/ void OpenNIFacade::internalSetup(AbstractConfiguration& configuration) throw(OpenNiException) {
	LOG->info("internalSetup()");

	LOG->debug("Creating depth generator ...");
	CHECK_XN(this->depthGenerator.Create(this->context), "Could not create depth generator!");

	XnBool xnMirrorMode = TRUE;
	CHECK_XN(this->context.SetGlobalMirror(), "Setting global mirror mode failed!");
	// TODO outsource as client configuration option

	// mandatory to set if starting non-recording, otherwise will fail
//	... if starting non-recording => CHECK_XN(this->depthGenerator.SetMapOutputMode(this->mapMode), "set depth mode");

	this->userManager = new UserManager(configuration.getUserCallback(), configuration.getJointCallback());
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
