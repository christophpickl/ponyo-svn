#include <ponyo/openni/PnOpenNI.hpp>

using namespace pn;

void onUserStateChanged(unsigned int userId, UserState userState) {
	printf("PlaygroundSample says: onUserStateChanged(userId=%i, userState=%i)\n", userId, userState);
}

OpenNIFacade g_facade(NULL, NULL);

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
	} catch(const OpenNiException& e) {
		e.printBacktrace();
	} catch(const Exception& e) {
		e.printBacktrace();
	} catch (const std::exception& e) {
		fprintf(stderr, "std exception: %s\n", e.what());
	} catch (...) {
		fprintf(stderr, "Unhandled exception!");
	}

	tearDown();

	LOG->info("main() END");
	return 0;
}
