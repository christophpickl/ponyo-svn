#include <stdio.h>
#include <ponyo/pnopenni/UserManager.hpp>

using namespace pn;
using namespace xn;

Context context;
DepthGenerator depthGenerator;
bool shouldTerminate = false;
XnMapOutputMode mapMode;

void onSignalReceived(int signalCode) {
	printf("onSignalReceived(signalCode=%d)\n", signalCode);
	shouldTerminate = true;
}

int main() {
	mapMode.nFPS = 30;
	mapMode.nXRes = XN_VGA_X_RES;
	mapMode.nYRes = XN_VGA_Y_RES;

	printf("main() START\n");
	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)

	CHECK_RC(context.Init(), "context.Init()");

	CHECK_RC(depthGenerator.Create(context), "create depth");
	CHECK_RC(depthGenerator.SetMapOutputMode(mapMode), "set depth mode"); // mandatory, otherwise will fail
	CHECK_RC(depthGenerator.StartGenerating(), "depth start");

	UserManager* mgr = new UserManager();
	mgr->init(context);
	mgr->start();

	printf("entering main loop...\n");
	while(!shouldTerminate) {
		CHECK_RC(context.WaitAnyUpdateAll(), "ctx.WaitAnyUpdateAll()");
	}

	mgr->stop();
	context.Shutdown();
	printf("main() END\n");
	return 0;
}
