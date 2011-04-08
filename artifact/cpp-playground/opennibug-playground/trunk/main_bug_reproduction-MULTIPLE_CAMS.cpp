#include <XnCppWrapper.h>
#include <vector>

#define CHECK(returnCode) \
	if(returnCode != XN_STATUS_OK) { \
		fprintf(stderr, "ERROR: %s\n", xnGetStatusString(returnCode)); \
		exit(1);\
	}

int main() {
	xn::Context context;
	printf("Initializing context\n");
	CHECK(context.Init());

	printf("Enumerating DEVICE nodes\n");
	xn::NodeInfoList deviceInfoList;
	CHECK(context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, NULL, deviceInfoList, NULL));
	std::vector<xn::NodeInfo> deviceNodes;
	for (xn::NodeInfoList::Iterator it = deviceInfoList.Begin(); it != deviceInfoList.End(); ++it) {
		deviceNodes.push_back(*it);
	}

	printf("Enumerating DEPTH nodes\n");
	xn::NodeInfoList depthInfoList;
	CHECK(context.EnumerateProductionTrees(XN_NODE_TYPE_DEPTH, NULL, depthInfoList, NULL));
	std::vector<xn::NodeInfo> depthNodes;
	for(xn::NodeInfoList::Iterator it = depthInfoList.Begin (); it != depthInfoList.End (); ++it) {
		depthNodes.push_back(*it);
	}

	printf("Enumerating IMAGE nodes\n");
	xn::NodeInfoList imageInfoList;
	CHECK(context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, NULL, imageInfoList, NULL));
	std::vector<xn::NodeInfo> imageNodes;
	for(xn::NodeInfoList::Iterator it = imageInfoList.Begin(); it != imageInfoList.End(); ++it) {
		imageNodes.push_back(*it);
	}

	for(int i=0, n=deviceNodes.size(); i < n; i++) {
		xn::NodeInfo deviceNode = deviceNodes[i];
		xn::NodeInfo depthNode = depthNodes[i];
		xn::NodeInfo imageNode = imageNodes[i];

		xn::DepthGenerator depthGenerator;
		xn::DepthGenerator imageGenerator;

		printf("Creating DEPTH node\n");
		CHECK(context.CreateProductionTree(const_cast<xn::NodeInfo&>(depthNode)));
		printf("Creating IMAGE node\n");
		// !!!!!!! THIS WILL FAIL !!!!!!! ===> "ERROR: Failed to set USB interface!"
		CHECK(context.CreateProductionTree(const_cast<xn::NodeInfo&>(imageNode)));

		printf("Getting DEPTH generator instance ...\n");
		CHECK(depthNode.GetInstance(depthGenerator));
		printf("Getting IMAGE generator instance ...\n");
		CHECK(imageNode.GetInstance(imageGenerator));
	}

	context.Shutdown();
	return 0;
}
