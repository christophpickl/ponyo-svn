// source: https://groups.google.com/group/openni-dev/tree/browse_frm/thread/dc92f6d1511c6bc1/f2e2bf1d06277b48?_done=%2Fgroup%2Fopenni-dev%2Fbrowse_frm%2Fthread%2Fdc92f6d1511c6bc1%2Ff2e2bf1d06277b48%3Ftvc%3D1%26&tvc=1

#include "myni_inc.h"
#include <sstream>

void dumpNode(xn::NodeInfo& node) {
	printf(" dump:\n\tCreationInfo: %s\n\tInstanceName: %s\n", node.GetCreationInfo(), node.GetInstanceName());
	const XnProductionNodeDescription descr = node.GetDescription();
	printf("\tDescription.Name: %s\n\tDescription.Type: %i\n\tDescription.Vendor: %s\n\n", descr.strName, descr.Type, descr.strVendor);
}

class Cam {
public:
	Cam(std::string pId, xn::Context& pContext) : id(pId), context(pContext) {
		printf("new Cam(id=[%s], deviceContext)\n", pId.c_str());
	}
	~Cam() {
		printf("~Cam() ... this->id=[%s]\n", this->id.c_str());
//		delete &this->context; // TODO was created on heap!
	}
	const xn::Context& getContext() const {
		return this->context;
	}
private:
	std::string id;
	xn::Context context;
};

int main() {
	printf("main() USB fail approach START\n");

	xn::Context globalContext;

	CHECK_RCX(globalContext.Init(), "globalContext.Init()");


	printf("\nGetting node infos\n");
	printf("=================================\n");

	std::vector<xn::NodeInfo> deviceNodes; xn::NodeInfoList deviceNodeInfoList;
	printf(">> Enumerate devices"); ENUMERATE_NODES(globalContext, "device", XN_NODE_TYPE_DEVICE, deviceNodes, deviceNodeInfoList);

	std::vector<xn::NodeInfo> depthNodes; xn::NodeInfoList depthNodeInfoList;
	printf(">> Enumerate depths"); ENUMERATE_NODES(globalContext, "depth", XN_NODE_TYPE_DEPTH, depthNodes, depthNodeInfoList);

	std::vector<xn::NodeInfo> imageNodes; xn::NodeInfoList imageNodeInfoList;
	printf(">> Enumerate images"); ENUMERATE_NODES(globalContext, "image", XN_NODE_TYPE_IMAGE, imageNodes, imageNodeInfoList);


	printf("\nCreating stuff for all devices\n");
	printf("=================================\n");
	const int n = deviceNodes.size();
	std::vector<Cam*> cams;
	for (int i = 0; i < n; ++i) {
		xn::NodeInfo deviceNode = deviceNodes[i];
		xn::NodeInfo depthNode = depthNodes[i];
		xn::NodeInfo imageNode = imageNodes[i];

		// deviceNode.GetCreationInfo() == 045e/02ae@36/6 or 045e/02ae@38/13
		std::stringstream sstream;
		sstream << "device(" << deviceNode.GetCreationInfo() << ")";
		std::string deviceId = sstream.str();

		printf("Setting up device '%s'\n", deviceId.c_str());
		printf("-----------------------------\n");

		printf("deviceNode"); dumpNode(deviceNode);
		printf("depthNode"); dumpNode(depthNode);
		printf("imageNode"); dumpNode(imageNode);

		xn::Context* deviceContext = new xn::Context();
		CHECK_RCX(deviceContext->Init(), "deviceContext->Init()");

		// ?? InitFromXmlFile??
		CHECK_RCX(deviceContext->CreateProductionTree(deviceNode), "deviceContext->CreateProductionTree(deviceNode)");
		CHECK_RCX(deviceContext->CreateProductionTree(depthNode), "deviceContext->CreateProductionTree(depthNode)");
		CHECK_RCX(deviceContext->CreateProductionTree(imageNode), "deviceContext->CreateProductionTree(imageNode)");

		cams.push_back(new Cam(deviceId, *deviceContext/*, *depthNode, *imageNode*/));
		printf("\n");
	}

	printf("\nShutting down\n");
	printf("=================================\n");
	for (int i = 0; i < n; ++i) {

		const Cam* cam = cams.at(i);
		xn::Context deviceContext = cam->getContext();

		printf(">> deviceContext.Shutdown() ...\n");
		deviceContext.Shutdown();

		delete cam;
	}

	printf(">> globalContext.Shutdown() ...\n"); globalContext.Shutdown();

	printf("\n\nmain() END\n");
	return 0;
}
