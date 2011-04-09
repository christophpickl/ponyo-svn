#include <stdio.h>
#include <XnCppWrapper.h>

#define CHECK(returnCode) \
if(returnCode != XN_STATUS_OK) { fprintf(stderr, "%s\n", xnGetStatusString(returnCode)); exit(1); }

xn::Context context;


//extern "C" void baz(/*type* var*/){
//   var->foo();
//}
extern "C" void init(){
	printf("C init()\n");
	CHECK(context.Init());
}

extern "C" int countDevices() {
	printf("C countDevices()\n");
	xn::NodeInfoList deviceNodes;
	XnStatus enumResult = context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, NULL, deviceNodes, NULL);
	if(enumResult == XN_STATUS_NO_NODE_PRESENT) {
		printf("C no device node present!\n");
		return 0;
	} else if(enumResult != XN_STATUS_OK) {
		fprintf(stderr, "C %s\n", xnGetStatusString(enumResult));
		return -1; // throw exception, or tha like
	}
	return 42;
}

extern "C" void shutdown(){
	printf("C shutdown()\n");
	context.Shutdown();
}


//PnJnaCpp::PnJnaCpp() {
//	printf("new PnJnaCpp()\n");
//}
//
//PnJnaCpp::~PnJnaCpp() {
//	printf("~PnJnaCpp()\n");
//}
//
//PN_C_API_EXPORT void PnJnaCpp::baz() {
//	printf("baaaz PnJnaC++\n");
//}
