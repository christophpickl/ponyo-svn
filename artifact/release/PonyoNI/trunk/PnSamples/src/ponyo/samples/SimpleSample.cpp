#include <ponyo/PnOpenNI.hpp>

using namespace pn;

void onUserStateChanged(UserId userId, UserState userState) {
	printf(">>>>> SimpleSample says: onUserStateChanged(userId=%i, userState=%i)\n", userId, userState);
}

void onJointPositionChanged(UserId userId, unsigned int jointId, float x, float y, float z) {
	printf(">>>>> SimpleSample says: onJointPositionChanged(userId=%i, jointId=%i, x/y/z=%f/%f/%f)\n", userId, jointId, x, y ,z);
}

int main() {
	printf("SimpleSample main() START\n");

	OpenNIFacade facade;

	StartXmlConfiguration configuration("misc/playground_config.xml", &onUserStateChanged, &onJointPositionChanged);
	facade.startWithXml(configuration);

	CommonUtils::waitHitEnter();
	facade.shutdown();

	printf("main() END\n");
	return 0;
}
