// source: https://groups.google.com/group/openni-dev/tree/browse_frm/thread/dc92f6d1511c6bc1/f2e2bf1d06277b48?_done=%2Fgroup%2Fopenni-dev%2Fbrowse_frm%2Fthread%2Fdc92f6d1511c6bc1%2Ff2e2bf1d06277b48%3Ftvc%3D1%26&tvc=1

#include "myni_inc.h"

int main() {
	printf("main() START\n");

	xn::Context globalContext;

	CHECK_RCX(globalContext.Init(), "globalContext.Init()");


	printf("\nGetting node infos\n");
	printf("=================================\n");

	std::vector<xn::NodeInfo> deviceNodes; xn::NodeInfoList deviceNodeInfoList;
	printf(">> Enumerate devices"); ENUMERATE_NODES(globalContext, "device", XN_NODE_TYPE_DEVICE, deviceNodes, deviceNodeInfoList);

//	std::vector<xn::NodeInfo> depthNodes; xn::NodeInfoList depthNodeInfoList;
//	printf(">> Enumerate depths"); ENUMERATE_NODES("depth", XN_NODE_TYPE_DEPTH, depthNodes, depthNodeInfoList);
//
//	std::vector<xn::NodeInfo> imageNodes; xn::NodeInfoList imageNodeInfoList;
//	printf(">> Enumerate images"); ENUMERATE_NODES("image", XN_NODE_TYPE_IMAGE, imageNodes, imageNodeInfoList);


	printf("\nCreating stuff for all devices\n");
	printf("=================================\n");
	const int n = deviceNodes.size();
	std::vector<xn::Context*> deviceContexts;
	for (int i = 0; i < n; ++i) {
		xn::NodeInfo deviceNode = deviceNodes[i];
//		xn::NodeInfo depthNode = depthNodes[i];
//		xn::NodeInfo imageNode = imageNodes[i];

		std::cout << "Iterating device " << node.GetCreationInfo() << std::endl;

		xn::Context* deviceContext = new xn::Context();
		deviceContexts.push_back(deviceContext);
		CHECK_RCX(deviceContext->Init(), "deviceContext->Init()");

		// TODO InitFromXmlFile??
		CHECK_RCX(deviceContext->CreateProductionTree(deviceNode), "deviceContext->CreateProductionTree(deviceNode)");

	}

	printf("\nShutting down\n");
	printf("=================================\n");
	for (int i = 0; i < n; ++i) {
		xn::Context* deviceContext = deviceContexts.at(i);
		printf(">> deviceContext.Shutdown() ...\n"); deviceContext.Shutdown();
		delete deviceContext;
	}

	printf(">> globalContext.Shutdown() ...\n"); globalContext.Shutdown();

	printf("main() END\n");
	return 0;
}
