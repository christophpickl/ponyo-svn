#include <ponyo/PnOpenNI.hpp>

using namespace pn;

OpenNIFacade g_facade;
Log* LOG = NEW_LOG();
int g_jointCounter = 0; // to print out every 100th joint change only

void onUserStateChanged(UserId userId, UserState userState) {
	printf(">>>>> PlaygroundSample says: onUserStateChanged(userId=%i, userState=%i)\n", userId, userState);
}

void onJointPositionChanged(UserId userId, unsigned int jointId, float x, float y, float z) {
	if(g_jointCounter == 100) {
		printf(">>>>> PlaygroundSample says: 100th onJointPositionChanged(userId=%i, jointId=%i, ...)\n", userId, jointId);
		g_jointCounter = 0;
	}
	g_jointCounter++;
}

void tearDown() {
	LOG->info("tearDown()");
	g_facade.shutdown();
}

void onSignalReceived(int signalCode) {
	LOG->info2("onSignalReceived(signalCode=%d)\n", signalCode);
	tearDown();
	printf("Terminating application by invoking exit()");
	exit(signalCode);
}

void mainJustStartContextAndDumpInfo() {
	LOG->info("mainJustStartContextAndDumpInfo()");

	xn::Context context;

//	const char* configXmlPath = "/ponyo/niconfig.xml";
//	const char* configXmlPath = "/ponyo/niconfig_corrupt.xml";
	const char* configXmlPath = "/NOT_EXISTING.xml";
	CHECK_XN(OpenNIUtils::safeInitFromXml(context, configXmlPath), "Could not initialize OpenNI context from XML!");

	OpenNIUtils::dumpNodeInfosByContext(context);
	context.Shutdown();
}

void mainInternal() {
	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)
//	OpenNIUtils::enableXnLogging(XN_LOG_INFO);

//	StartXmlConfig config("misc/playground_config.xml", &onUserStateChanged, &onJointPositionChanged);
//	config.setMirrorModeEnableb(true);
//	config.setImageGeneratorEnabled(true);
//	g_facade.startWithXml(config);

	StartOniConfig config("/ponyo/oni.oni", &onUserStateChanged, &onJointPositionChanged);
	g_facade.startRecording(config);

	printf("Hit ENTER to quit\n");
	CommonUtils::waitHitEnter(false);
	printf("ENTER pressed, shutting down.\n");

	tearDown();
}

int main() {
	LOG->info("PlaygroundSample main() START");
	try {

//		mainInternal();
		mainJustStartContextAndDumpInfo();

	} catch(Exception& e) {
		fprintf(stderr, "Ponyo custom Exception was thrown!\n");
		e.printBacktrace();
	} catch(std::exception& e) {
		fprintf(stderr, "std::exception: %s\n", e.what());
	} catch(...) {
		fprintf(stderr, "Some unkown error occured! DEBUG!"); // TODO how to process varargs?!
	}
	LOG->info("main() END");
	return 0;
}
