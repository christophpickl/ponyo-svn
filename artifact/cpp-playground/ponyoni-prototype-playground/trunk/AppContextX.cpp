#include <stdio.h>
#include <ponyo/pnopenni/simplified/ContextX.hpp>

using namespace pn;
using namespace xn;

ContextX* context;
bool shouldTerminate = false;

void onSignalReceived(int signalCode) {
	printf("onSignalReceived(signalCode=%d)\n", signalCode);
	shouldTerminate = true;
}

int main() {
	printf("main() START\n");
	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)

	context = new ContextX();
	context->init();
	context->start();

	printf("entering main loop...\n");
	while(!shouldTerminate) {
		context->waitAndUpdate();
	}

	context->shutdown();
	delete context;

	printf("main() END\n");
	return 0;
}
