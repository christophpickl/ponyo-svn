#include <boost/thread.hpp>
#include <ponyo/PnOpenNI.hpp>

using namespace pn;

OpenNIFacade g_facade;
Log* LOG = NEW_LOG();
int g_jointCounter = 0; // to print out every 100th joint change only

boost::thread g_secondMainThread;

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
	LOG->info2("onSignalReceived(signalCode=%d)", signalCode);
	tearDown();
	printf("Terminating application by invoking exit()\n");
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

void onThreadRun() {
	LOG->info("onThreadRun()");


	CommonUtils::sleep(2);
	g_facade.setWindowVisible(false);
	CommonUtils::sleep(2);
	printf("XXXXXXXXXXXXXXXXXXXXXXXXXX reshowing window\n");
	g_facade.setWindowVisible(true);
	printf("XXXXXXXXXXXXXXXXXXXXXXXXXX reshowing window END\n");
	CommonUtils::sleep(2);
	g_facade.setWindowVisible(false);

	printf("Hit ENTER to quit\n");
	CommonUtils::waitHitEnter(false);
	printf("ENTER pressed, shutting down.\n");

	tearDown();

	LOG->debug("invoking exit(0), as glut could still block main loop");
	exit(0);
}

void mainInternal() {
	printf(__FILE__ "#mainInternal()\n");
//	OpenNIUtils::enableXnLogging(XN_LOG_INFO);

	StartXmlConfig config("misc/playground_config.xml", &onUserStateChanged, &onJointPositionChanged);
//	StartOniConfig config("/ponyo/oni.oni", &onUserStateChanged, &onJointPositionChanged);
	config.setImageGeneratorEnabled(true);
	config.setDepthGeneratorEnabled(false);
	config.setUserGeneratorEnabled(false);
	g_facade.startWithXml(config);
//	g_facade.startRecording(config);

	LOG->debug("Spawning new thread ...");
	g_secondMainThread = boost::thread(&onThreadRun);

	printf("displaying window ...\n");
	g_facade.setWindowVisible(true);
}

void onWindowAction(WindowAction actionId) {
	LOG->debug2("onWindowAction(actionId=%i)", actionId);
	switch(actionId) {
	case WINDOW_ACTION_ESCAPE:
		onSignalReceived(0);
		break;
	default:
		LOG->warn2("Unhandled window action [%i]!", actionId);
	}
}
int main(int argc, char** argv) {
	LOG->info("PlaygroundSample main() START");

	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)

	try {
//		ImageWindow* win = ImageWindow::getInstance(&onWindowAction);
//		win->init(argc, argv);
//		win->setWindowVisible(true);
//		ImageWindow::destroy();

		mainInternal();
//		mainJustStartContextAndDumpInfo();

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
