#include <ponyo/openni/PnOpenNI.hpp>

using namespace pn;

void onUserStateChanged(unsigned int userId, UserState userState) {
	printf(">>>>> SimpleSample says: onUserStateChanged(userId=%i, userState=%i)\n", userId, userState);
}

void onJointDataChanged(UserId userId, unsigned int jointId, float x, float y, float z) {
	printf(">>>>> SimpleSample says: onJointDataChanged(userId=%i, jointId=%i, x=%f)\n", userId, jointId, x);
}

int main() {
	printf("SimpleSample main() START\n");

	OpenNIFacade* facade = new OpenNIFacade(&onUserStateChanged, &onJointDataChanged);

//	facade->startWithXml("misc/playground_config.xml");
	facade->startRecording("/myopenni/myoni.oni");
	CommonUtils::waitHitEnter();
	facade->destroy();
	delete facade;

	printf("main() END\n");
	return 0;
}
