#include <ponyo/osc/PnOSC.hpp>


using namespace pn;

void tearDown() {
//	LOG->info("tearDown()");
//	g_facade.destroy();
}

void onSignalReceived(int signalCode) {
	printf("onSignalReceived(signalCode=%d)\n", signalCode);
	tearDown();
}

int main() {
	printf("PnOSC server starting ...\n");

	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)

	PnOSC* osc = new PnOSC();
	osc->startup();
	delete osc;

	printf("PnOSC server stopping ...\n");
	return 0;
}
