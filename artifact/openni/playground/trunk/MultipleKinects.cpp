#include <vector.h>
#include "ponyo_common.h"
#include "NiDevice.h"
#include "MultipleKinects.h"


MultipleKinects::MultipleKinects() {
	printf("new MultipleKinects()\n");
}

MultipleKinects::~MultipleKinects() {
	for (unsigned i = 0; i < this->devices.size(); i++) {
		delete this->devices[i];
	}

	printf("delete ~MultipleKinects()\n");
}

// most code came from: http://groups.google.com/group/openni-dev/browse_thread/thread/cde3217c242a3687/3c1463337f6f2951?lnk=gst&q=multiple#3c1463337f6f2951
void MultipleKinects::initFromXml(std::string xmlConfigPath) {
	printf("MultipleKinects.initFromXml(xmlConfigPath)\n");

	XnStatus returnCode;

	printf("Initializing context ...\n");
	returnCode = this->context.Init();
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Initialisation failed!", returnCode); }

	printf("Looking for connected devices...\n");
	static xn::NodeInfoList deviceInfoList;
	returnCode = this->context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, NULL, deviceInfoList);
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Enumerating device nodes failed!", returnCode); }

	if(deviceInfoList.Begin() == deviceInfoList.End()) {
		std::string errorMsg = "Device node list is empty! (Probably no devices connected?!)";
		throw errorMsg;
	}
	vector<xn::NodeInfo> deviceInfos;
	int c = 0; // i'd like to have a "c++" ;)
	for (xn::NodeInfoList::Iterator it = deviceInfoList.Begin(); it != deviceInfoList.End(); ++it) {
		deviceInfos.push_back(*it);
		c++; // yeah!!! :)))
	}
	printf("Connected devices: %i\n", c);

//	printf("Looking for available depth generators...\n");
//	static xn::NodeInfoList depth_nodes;
//	status = context_.EnumerateProductionTrees (XN_NODE_TYPE_DEPTH, NULL, depth_nodes, NULL);
//	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Enumerating depth generator nodes failed!", returnCode); }
//	vector<xn::NodeInfo> depth_info;
//	for (xn::NodeInfoList::Iterator nodeIt = depth_nodes.Begin (); nodeIt != depth_nodes.End (); ++nodeIt) {
//		depth_info.push_back (*nodeIt);
//	}

	printf("Looking for available image generators...\n");
	static xn::NodeInfoList imageInfoList;
	returnCode = this->context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, NULL,imageInfoList, NULL);
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Enumerating image nodes failed!", returnCode); }

	vector<xn::NodeInfo> imageInfos;
	for (xn::NodeInfoList::Iterator it = imageInfoList.Begin(); it != imageInfoList.End(); ++it) {
		imageInfos.push_back(*it);
	}

	if(deviceInfos.size() != imageInfos.size() /* || dev.size != depth.size */) {
		std::string errorMsg = "Number of devices does not match number of image streams!";
		throw errorMsg;
	}

	for (unsigned i = 0; i < deviceInfos.size(); i++) {
		printf("Processing device number %i#.\n", (i+1));
		xn::NodeInfo deviceInfo = deviceInfos[i];
		xn::NodeInfo imageInfo = imageInfos[i];

	//	returnCode = this->context.CreateProductionTree(const_cast<xn::NodeInfo&>(deviceInfo));
	//	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Creating depth generator failed!", returnCode); }

		printf("Creating image generator production node...\n");
		returnCode = this->context.CreateProductionTree(const_cast<xn::NodeInfo&>(imageInfo));
		if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Creating image generator instance failed!", returnCode); }

		// get production node instances
	//	returnCode = depth_node.GetInstance(depth_generator_);
	//	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Creating depth generator instance failed!", returnCode); }

		xn::ImageGenerator imageGenerator;
		returnCode = imageInfo.GetInstance (imageGenerator);
		if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Creating image generator instance failed!", returnCode); }

		NiDevice* device = new NiDevice(deviceInfo, imageInfo, imageGenerator);
		device->printToString();
//		device->start();
		this->devices.push_back(device);
	}
}

void MultipleKinects::waitForUpdate() {
	printf("MultipleKinects.waitForUpdate()\n");

}

void MultipleKinects::close() {
	printf("MultipleKinects.close()\n");

}
