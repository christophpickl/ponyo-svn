#include <ponyo/openni/PnOpenNI.hpp>
using namespace pn;

bool g_shouldQuit = false;

void onSignalReceived(int signalCode) {
	printf("onSignalReceived(signalCode=%d)\n", signalCode);
	g_shouldQuit = true;
}

void foo() {
	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)

	PnContext* context = new PnContext();
	try {
		context->startRecording("/myopenni/myoni.oni");
		printf("Entering infinite loop ...\n");
		int i = 0;
		while(g_shouldQuit == false && i != 5) {
			Utils::sleep(2);
			i++;
		}
		printf("loop ended\n");
	} catch(const Exception& e) {
		e.printBacktrace();
	}

	context->destroy();
	delete context;
}

int main() {
	printf("PlaygroundSample main() START\n");

	foo();

	printf("main() END\n");
	return 0;
}
