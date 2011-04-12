#include <ponyo/openni/PnOpenNI.hpp>

using namespace pn;

OpenNIFacade g_facade;

Log* LOG = NEW_LOG();

void tearDown() {
	LOG->info("tearDown()");
	g_facade.destroy();
}

void onSignalReceived(int signalCode) {
	printf("onSignalReceived(signalCode=%d)\n", signalCode);
	tearDown();
}

int main() {
	LOG->info("PlaygroundSample main() START");

	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)
//	OpenNIUtils::enableXnLogging(XN_LOG_INFO);

	try {
		g_facade.startRecording("/myopenni/myoni.oni");
//		g_facade.startWithXml("misc/playground_config.xml");

		printf("Hit ENTER to quit\n");
		CommonUtils::waitHitEnter(false);
		printf("ENTER pressed, shutting down.\n");

	} catch(const Exception& e) {
		e.printBacktrace();
	}

	tearDown();

	LOG->info("main() END");
	return 0;
}
