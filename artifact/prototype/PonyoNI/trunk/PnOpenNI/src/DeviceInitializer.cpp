#include "common_openni.hpp"
#include "DeviceInitializer.hpp"

namespace pn {

Log* DeviceInitializer::LOG = NEW_LOG(__FILE__)

DeviceInitializer::DeviceInitializer() {
	LOG->debug("new DeviceInitializer()");
}

DeviceInitializer::~DeviceInitializer() {
	LOG->debug("~DeviceInitializer()");
}

void DeviceInitializer::fetchDevices(xn::Context& context) {
	LOG->debug("fetchDevices(context)");

	xn::NodeInfoList deviceInfoList;
	// XnStatus EnumerateProductionTrees(XnProductionNodeType Type, Query* pQuery, NodeInfoList& TreesList, EnumerationErrors* pErrors = NULL) const
	LOG->trace(">> this->context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, ..)");
	CHECK_RC(context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, NULL, deviceInfoList), "context.EnumerateProductionTrees(..)");

	if(deviceInfoList.Begin() == deviceInfoList.End()) {
		println("Device node list is empty! (Probably no devices connected?!)");
		return;
	}

//	vector<xn::NodeInfo> deviceInfos;
	int c = 0; // i'd like to have a "c++" ;)
	for (xn::NodeInfoList::Iterator it = deviceInfoList.Begin(); it != deviceInfoList.End(); ++it) {
		xn::NodeInfo info = *it;
		printf("info.x=%i\n", info.GetDescription().strVendor);
//		deviceInfos.push_back(*it);
//		c++; // yeah!!! :)))
	}
}

}
