#include <XnCppWrapper.h>

#define CHECK(returnCode) \
if(returnCode != XN_STATUS_OK) { fprintf(stderr, "%s\n", xnGetStatusString(returnCode)); exit(1); }

int main() {
	xn::Context context;
	printf("Init");
	CHECK(context.Init());

	printf("Getting DEPTH node\n");
	xn::NodeInfoList depthNodes;
	CHECK(context.EnumerateProductionTrees(XN_NODE_TYPE_DEPTH, NULL, depthNodes, NULL));
	xn::NodeInfo depthNode = *depthNodes.Begin();

	printf("Getting IMAGE node\n");
	xn::NodeInfoList imageNodes;
	CHECK(context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, NULL, imageNodes, NULL));
	xn::NodeInfo imageNode = *imageNodes.Begin();

	printf("Creating IMAGE node\n");
	CHECK(context.CreateProductionTree(imageNode));

	printf("Creating DEPTH node\n");
	CHECK(context.CreateProductionTree(depthNode)); // !!! ERROR !!! ==> "Failed to set USB interface!"

	printf("Shutdown");
	context.Shutdown();
	return 0;
}
