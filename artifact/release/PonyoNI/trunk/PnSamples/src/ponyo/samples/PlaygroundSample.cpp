#include <ponyo/PnOpenNI.hpp>

using namespace pn;

void onUserStateChanged(UserId userId, UserState userState) {
	printf("PlaygroundSample says: onUserStateChanged(userId=%i, userState=%i)\n", userId, userState);
}

int g_jointCounter = 0;
void onJointPositionChanged(UserId userId, unsigned int jointId, float x, float y, float z) {
	if(g_jointCounter == 100) {
		printf("PlaygroundSample says: 100th onJointPositionChanged(userId=%i, jointId=%i, ...)\n", userId, jointId);
		g_jointCounter = 0;
	}
	g_jointCounter++;
}

OpenNIFacade g_facade(&onUserStateChanged, &onJointPositionChanged);

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

	g_facade.startRecording("/ponyo/oni.oni");
//	g_facade.startWithXml("misc/playground_config.xml");

	printf("Hit ENTER to quit\n");
	CommonUtils::waitHitEnter(false);
	printf("ENTER pressed, shutting down.\n");

	tearDown();

	LOG->info("main() END");
	return 0;
}
