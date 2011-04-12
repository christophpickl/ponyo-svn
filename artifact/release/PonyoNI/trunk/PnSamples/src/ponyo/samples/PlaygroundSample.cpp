#include <ponyo/openni/PnOpenNI.hpp>
using namespace pn;

PnContext g_context;

Log* LOG = NEW_LOG();

void tearDown() {
	LOG->info("tearDown()");
	g_context.destroy();
}

void onSignalReceived(int signalCode) {
	printf("onSignalReceived(signalCode=%d)\n", signalCode);
	tearDown();
}

int main() {
	LOG->info("PlaygroundSample main() START\n");

	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)
//	OpenNIUtils::enableXnLogging(XN_LOG_INFO);

	try {
//		g_context.startRecording("/myopenni/myoni.oni");
		g_context.startWithXml("/myopenni/simple_config.xml");

		printf("Hit ENTER to quit\n");
		std::string input;
		std::getline(std::cin, input);
		printf("ENTER pressed, shutting down.\n");

	} catch(const Exception& e) {
		e.printBacktrace();
	}

	tearDown();

	LOG->info("main() END\n");
	return 0;
}
