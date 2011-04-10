#include <ponyo/pnopenni/PnOpenNI.hpp>

namespace pn {

void baaar() {
	xn::Context ctx;

	CHECK_RC(ctx.Init(), "init");
	xn::NodeInfoList userNodes;

	CHECK_RC(ctx.EnumerateProductionTrees(XN_NODE_TYPE_USER, NULL, userNodes, NULL), "enum user");

	xn::NodeInfo userNode = *userNodes.Begin();
	CHECK_RC(ctx.CreateProductionTree(userNode), "create user node");

	xn::UserGenerator userGenerator;
	CHECK_RC(userNode.GetInstance(userGenerator), "create user instance");

	CHECK_RC(ctx.StartGeneratingAll(), "ctx start");
//	CHECK_RC(userGenerator.StartGenerating(), "user start");
//	userGenerator.
	CHECK_RC(userGenerator.StopGenerating(), "user end");

	ctx.Shutdown();
//	xn::NodeInfo;
}

void reproduceCreateProductionTreeError() {
	XnMapOutputMode mapMode; mapMode.nFPS = 30; mapMode.nXRes = 640; mapMode.nYRes = 480;

	printf("Initializing context ...\n"); xn::Context context; CHECK_RC(context.Init(), "context.Init()");

	printf("Enumerating DEVICE nodes ...\n"); static xn::NodeInfoList deviceInfoList; CHECK_RC(context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, NULL, deviceInfoList, NULL), "EnumerateProductionTrees(DEVICE)");
	std::vector<xn::NodeInfo> deviceNodes; for (xn::NodeInfoList::Iterator nodeIt = deviceInfoList.Begin (); nodeIt != deviceInfoList.End (); ++nodeIt) { deviceNodes.push_back(*nodeIt); }

	printf("Enumerating DEPTH nodes ...\n"); static xn::NodeInfoList depthInfoList; CHECK_RC(context.EnumerateProductionTrees(XN_NODE_TYPE_DEPTH, NULL, depthInfoList, NULL), "EnumerateProductionTrees(DEPTH)");
	std::vector<xn::NodeInfo> depthNodes; for (xn::NodeInfoList::Iterator nodeIt = depthInfoList.Begin (); nodeIt != depthInfoList.End (); ++nodeIt) { depthNodes.push_back(*nodeIt); }

	printf("Enumerating IMAGE nodes ...\n"); static xn::NodeInfoList imageInfoList; CHECK_RC(context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, NULL, imageInfoList, NULL), "EnumerateProductionTrees(IMAGE)");
	std::vector<xn::NodeInfo> imageNodes; for (xn::NodeInfoList::Iterator nodeIt = imageInfoList.Begin (); nodeIt != imageInfoList.End (); ++nodeIt) { imageNodes.push_back(*nodeIt); }

	printf("deviceNodes.size = %i\n", (int) deviceNodes.size());
	printf("depthNodes.size = %i\n", (int) depthNodes.size());
	printf("imageNodes.size = %i\n", (int) imageNodes.size());

	for(int i=0, n=deviceNodes.size(); i < n; i++) {
		xn::NodeInfo deviceNode = deviceNodes[i];
		xn::NodeInfo depthNode = depthNodes[i];
		xn::NodeInfo imageNode = imageNodes[i];

		xn::DepthGenerator depthGenerator;
		xn::DepthGenerator imageGenerator;

		printf("Creating DEPTH node ...\n"); CHECK_RC(context.CreateProductionTree(const_cast<xn::NodeInfo&>(depthNode)), "CreateProductionTree(DEPTH)");
		printf("Creating IMAGE node ...\n"); CHECK_RC(context.CreateProductionTree(const_cast<xn::NodeInfo&>(imageNode)), "CreateProductionTree(IMAGE)");
//		printf("Creating DEPTH node ...\n"); CHECK_RC(context.CreateAnyProductionTree(XN_NODE_TYPE_DEPTH, NULL, depthGenerator, NULL), "CreateProductionTree(DEPTH)");
//		printf("Creating IMAGE node ...\n"); CHECK_RC(context.CreateAnyProductionTree(XN_NODE_TYPE_IMAGE, NULL, imageGenerator, NULL), "CreateProductionTree(IMAGE)");

		printf("Getting DEPTH generator instance ...\n"); CHECK_RC(depthNode.GetInstance(depthGenerator), "GetInstance(DEPTH)");
		printf("Getting IMAGE generator instance ...\n"); CHECK_RC(imageNode.GetInstance(imageGenerator), "GetInstance(IMAGE)");

		printf(">> imageGenerator.GetSupportedMapOutputModesCount()\n"); unsigned modeCount = imageGenerator.GetSupportedMapOutputModesCount();
		XnMapOutputMode* modes = new XnMapOutputMode[modeCount];
		printf(">> imageGenerator.GetSupportedMapOutputModes(modes, modeCount)\n"); XnStatus status = imageGenerator.GetSupportedMapOutputModes(modes, modeCount);

		for (unsigned modeIdx = 0; modeIdx < modeCount; ++modeIdx) {
			printf("=> %i: fps=%i, size=%ix%i\n", (modeIdx+1), modes[modeIdx].nFPS, modes[modeIdx].nXRes, modes[modeIdx].nYRes);
//			available_image_modes_.push_back (modes[modeIdx]);
		}
		delete[] modes;

		printf("Setting DEPTH map output mode ...\n"); CHECK_RC(depthGenerator.SetMapOutputMode(mapMode), "depthGenerator.SetMapOutputMode(..)");
		printf("Setting IMAGE map output mode ...\n"); CHECK_RC(imageGenerator.SetMapOutputMode(mapMode), "imageGenerator.SetMapOutputMode(..)");
	}

	printf("Shutting down context ...\n"); context.Shutdown();
}

//int main(int argc, char** argv) {
//	println("main() START");
//	playground();
//	println("main() END");
//	return 0;
//}

}
