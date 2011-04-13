#include <ponyo/PnOpenNI.hpp>

using namespace pn;

void onUserStateChanged(unsigned int userId, UserState userState) {
	printf(">>>>> SimpleSample says: onUserStateChanged(userId=%i, userState=%i)\n", userId, userState);
}

void onJointPositionChanged(UserId userId, unsigned int jointId, float x, float y, float z) {
	printf(">>>>> SimpleSample says: onJointPositionChanged(userId=%i, jointId=%i, x=%f)\n", userId, jointId, x);
}

void usePnFacadeToRunOniFile() {
	OpenNIFacade* facade = new OpenNIFacade(&onUserStateChanged, &onJointPositionChanged);

//	facade->startWithXml("misc/playground_config.xml");
	facade->startRecording("/myopenni/myoni.oni");
	CommonUtils::waitHitEnter();
	facade->destroy();
	delete facade;
}

int main() {
	printf("SimpleSample main() START\n");
	try {
//		usePnFacadeToRunOniFile();

		xn::Context context;
//		CHECK_XN(context.Init(), "context init");
		CHECK_XN(context.InitFromXmlFile("/ponyo/niconfig.xml"), "context init xml");
		OpenNIUtils::dumpNodeInfosByContext(context);
		context.Shutdown();
	} catch(Exception& e) {
		fprintf(stderr, "Ponyo Exception:");
		e.printBacktrace();
	} catch(std::exception& e) {
		fprintf(stderr, "std::exception: %s\n", e.what());
	} catch(...) {
		fprintf(stderr, "Some unkown error occured! DEBUG!"); // TODO how to process varargs?!
	}
	printf("main() END\n");
	return 0;
}
