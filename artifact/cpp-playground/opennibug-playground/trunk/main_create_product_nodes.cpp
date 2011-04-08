#include "myni_inc.h"

xn::Context context;

//	XN_NODE_TYPE_DEVICE = 1,
//	XN_NODE_TYPE_DEPTH = 2,
//	XN_NODE_TYPE_IMAGE = 3,
//	XN_NODE_TYPE_AUDIO = 4,
//	XN_NODE_TYPE_IR = 5,
//	XN_NODE_TYPE_USER = 6,
//	XN_NODE_TYPE_RECORDER = 7,
//	XN_NODE_TYPE_PLAYER = 8,
//	XN_NODE_TYPE_GESTURE = 9,
//	XN_NODE_TYPE_SCENE = 10,
//	XN_NODE_TYPE_HANDS = 11,
//	XN_NODE_TYPE_CODEC = 12,
void dumpNode(xn::NodeInfo& node) {
	printf("dumpNode:\n\tCreationInfo: %s\n\tInstanceName: %s\n", node.GetCreationInfo(), node.GetInstanceName());

	const XnProductionNodeDescription descr = node.GetDescription();
	printf("\tDescription.Name: %s\n\tDescription.Type: %i\n\tDescription.Vendor: %s\n\n", descr.strName, descr.Type, descr.strVendor);

//	"045e/02ae@36/6"
//	const XnChar* creationInfoXn = deviceInfo.GetCreationInfo(); // \\?\usb#vid_045e&pid_02ae#a00362....102a#{.(some more numbers)..}
}

void foo1() {
	printf("foo1() START\n");

	std::vector<xn::NodeInfo> deviceInfos; xn::NodeInfoList deviceNodeInfoList;
	ENUMERATE_NODES(context, "device", XN_NODE_TYPE_DEVICE, deviceInfos, deviceNodeInfoList);

	const int n = deviceInfos.size();
	for (int i = 0; i < n; ++i) {
		printf("\nSetting up generators for device %i of %i ...\n", (i+1), n);
		xn::NodeInfo deviceInfo = deviceInfos[i];
		dumpNode(deviceInfo);

//		xn::Query* depthQuery = new xn::Query();
//		const XnChar* depthQueryCreationInfo = deviceInfo.GetCreationInfo(); //"045e/02ae@38/12";
//		depthQuery->SetCreationInfo(depthQueryCreationInfo);
//		const XnChar* depthQueryVendor = deviceInfo.GetDescription().strVendor; // "PrimeSense"
//		depthQuery->SetVendor(depthQueryVendor);
//		depthQuery->AddNeededNode(const XnChar*)
//		depthQuery->AddSupportedCapability(const XnChar*)
//		depthQuery->AddSupportedMapOutputMode(const XnMapOutputMode&)
//		depthQuery->SetExistingNodeOnly(XnBool)
//		depthQuery->SetMaxVersion(XnVersion&)
//		depthQuery->SetMinVersion(const XnVersion&)
//		depthQuery->SetName(const XnChar*) // SensorV2
//		depthQuery->SetSupportedMinUserPositions(unsigned int count)
		// EnumerationErrors* pErrors;
//		xn::DepthGenerator depthGen; CHECK_RC(depthGen.Create(context, depthQuery), "depthGen.Create(context, depthQuery)");

		xn::DepthGenerator depthGen; printf("depthGen.Create(context)"); CHECK_RC(depthGen.Create(context), "depthGen.Create(context)");
		xn::NodeInfo depthInfo = depthGen.GetInfo(); dumpNode(depthInfo);

		xn::ImageGenerator imageGen; printf("imageGen.Create(context)"); CHECK_RC(imageGen.Create(context), "imageGen.Create(context)");
		xn::NodeInfo imageInfo = imageGen.GetInfo(); dumpNode(imageInfo);
	}

	printf("foo1() END\n");
}

void foo2() {
	printf("foo2() START\n");

	printf("\nGetting node infos\n");
	printf("=================================\n");

	std::vector<xn::NodeInfo> deviceNodes; xn::NodeInfoList deviceNodeInfoList;
	printf(">> Enumerate devices"); ENUMERATE_NODES(context, "device", XN_NODE_TYPE_DEVICE, deviceNodes, deviceNodeInfoList);

	std::vector<xn::NodeInfo> depthNodes; xn::NodeInfoList depthNodeInfoList;
	printf(">> Enumerate depths"); ENUMERATE_NODES(context, "depth", XN_NODE_TYPE_DEPTH, depthNodes, depthNodeInfoList);

	std::vector<xn::NodeInfo> imageNodes; xn::NodeInfoList imageNodeInfoList;
	printf(">> Enumerate images"); ENUMERATE_NODES(context, "image", XN_NODE_TYPE_IMAGE, imageNodes, imageNodeInfoList);


	printf("\nCreating generators\n");
	printf("=================================\n");
	const int n = deviceNodes.size();
	for (int i = 0; i < n; ++i) {
		xn::NodeInfo deviceNode = deviceNodes[i];
		xn::NodeInfo depthNode = depthNodes[i];
		xn::NodeInfo imageNode = imageNodes[i];

		char depthInstanceName[20];
		if(snprintf(depthInstanceName, 20, "depth%i", i) < 0) { fprintf(stderr, "ups! snprintf failed!\n"); exit(1); }
		depthNode.SetInstanceName(depthInstanceName);

		char imageInstanceName[20];
		if(snprintf(imageInstanceName, 20, "image%i", i) < 0) { fprintf(stderr, "ups! snprintf failed!\n"); exit(1); }
		imageNode.SetInstanceName(imageInstanceName);

		dumpNode(deviceNode);
		dumpNode(depthNode);
		dumpNode(imageNode);

		printf(">> CreateProductionTree(DEPTH)"); CHECK_RC(context.CreateProductionTree(depthNode), "CreateProductionTree(DEPTH)");
		// FAIL: second CreateProductionTree call: Failed to set USB device!
		printf(">> CreateProductionTree(IMAGE)"); CHECK_RC(context.CreateProductionTree(imageNode), "CreateProductionTree(IMAGE)");

		// GetInstance
	}

	printf("foo2() END\n");
}

int main() {
	printf("main() START\n");

	printf("context.Init()"); CHECK_RC(context.Init(), "context.Init()");

	// XnMapOutputMode mapMode; mapMode.nXRes = XN_VGA_X_RES; mapMode.nYRes = XN_VGA_Y_RES; mapMode.nFPS = 30;
//	CHECK_RC(depthGen.SetMapOutputMode(mapMode), "depthGen.SetMapOutputMode(mapMode)");
//	CHECK_RC(imageGen.SetMapOutputMode(mapMode), "imageGen.SetMapOutputMode(mapMode)");

//	foo1();
	foo2();

	printf(">> context.Shutdown() ...\n");
	context.Shutdown();

	printf("main() END\n");
	return EXIT_SUCCESS;
}

/*
if (nRetVal == XN_STATUS_NO_NODE_PRESENT) {
	// Iterate over enumeration errors, and print each one
	for (xn::EnumerationErrors::Iterator it = errors.Begin(); it != errors.End(); ++it) {
		XnChar strDesc[512];
		xnProductionNodeDescriptionToString(&it.Description(), strDesc, 512);
		printf("%s failed to enumerate: %s\n", xnGetStatusString(it.Error()));
	}
	return (nRetVal);
} else if (nRetVal != XN_STATUS_OK) {
	throw error ...
}
 */
