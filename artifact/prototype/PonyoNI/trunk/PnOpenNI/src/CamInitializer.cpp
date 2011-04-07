#include "CamInitializer.hpp"

namespace pn {

Log* CamInitializer::LOG = NEW_LOG(__FILE__)

CamInitializer::CamInitializer() {
	LOG->debug("new CamInitializer(context)");
}

CamInitializer::~CamInitializer() {
	LOG->debug("~CamInitializer()");
}

void CamInitializer::fetchDevices(xn::Context& context) {
	LOG->debug("fetchDevices(context)");

	this->workerThread = boost::thread(&CamInitializer::run, this, context);
}

/*private*/ void CamInitializer::run(xn::Context& context) {
	LOG->debug("run() ... THREAD started");

	std::vector<Cam*> cams;

	xn::NodeInfoList deviceInfoList;
	// XnStatus EnumerateProductionTrees(XnProductionNodeType Type, Query* pQuery, NodeInfoList& TreesList, EnumerationErrors* pErrors = NULL) const
	LOG->trace(">> context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, ..)");

	XnStatus returnCode = context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, NULL, deviceInfoList);
	if(returnCode != XN_STATUS_OK) {
		printf("Fetching devices failed (Probably no devices connected?!)\nOpenNI error message: %s\n", xnGetStatusString(returnCode));
		this->dispatchEvent(cams);
		return;
	}

	if(deviceInfoList.Begin() == deviceInfoList.End()) {
		println("Device node list is empty! (Probably no devices connected?!)");
		this->dispatchEvent(cams);
		return;
	}

	std::vector<xn::NodeInfo> deviceInfos;
	int cnt = 0;
	for (xn::NodeInfoList::Iterator it = deviceInfoList.Begin(); it != deviceInfoList.End(); ++it) {
		xn::NodeInfo info = *it;
		printf("\t%i. info.x=%s\n", (cnt+1), info.GetDescription().strName);
		deviceInfos.push_back(*it);
		cnt++;
	}
	printf("Found devices: %i\n", cnt);

	//	printf("Looking for available depth generators...\n");
	//	static xn::NodeInfoList depth_nodes;
	//	status = context_.EnumerateProductionTrees (XN_NODE_TYPE_DEPTH, NULL, depth_nodes, NULL);
	//	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Enumerating depth generator nodes failed!", returnCode); }
	//	vector<xn::NodeInfo> depth_info;
	//	for (xn::NodeInfoList::Iterator nodeIt = depth_nodes.Begin (); nodeIt != depth_nodes.End (); ++nodeIt) {
	//		depth_info.push_back (*nodeIt);
	//	}

	xn::NodeInfoList imageInfoList;
	LOG->trace(">> context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, ..)");
	CHECK_RC(context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, NULL,imageInfoList, NULL), "context.EnumerateProductionTrees(IMAGE)");

	std::vector<xn::NodeInfo> imageInfos;
	for (xn::NodeInfoList::Iterator it = imageInfoList.Begin(); it != imageInfoList.End(); ++it) {
		imageInfos.push_back(*it);
	}

	if(deviceInfos.size() != imageInfos.size() /* || dev.size != depth.size */) {
		throw OpenNiException("Number of devices does not match number of image streams!", AT);
	}

	for (unsigned i = 0, n = deviceInfos.size(); i < n; i++) {
		printf("Processing device number %i of %i\n", (i+1), n);
		xn::NodeInfo deviceInfo = deviceInfos[i];
		xn::NodeInfo imageInfo = imageInfos[i];

	//	returnCode = this->context.CreateProductionTree(const_cast<xn::NodeInfo&>(deviceInfo));
	//	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Creating depth generator failed!", returnCode); }

		LOG->trace(">> context.CreateProductionTree(imageInfo)");
		CHECK_RC(context.CreateProductionTree(const_cast<xn::NodeInfo&>(imageInfo)), "Creating image generator instance failed!");

		// get production node instances
	//	returnCode = depth_node.GetInstance(depth_generator_);
	//	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Creating depth generator instance failed!", returnCode); }

		xn::ImageGenerator imageGenerator;
		LOG->trace(">> imageInfo.GetInstance(imageGenerator)");
		CHECK_RC(imageInfo.GetInstance(imageGenerator), "Creating image generator instance failed!");



		// call virtual function to find available modes specifically for each device type
	//	this->getAvailableModes();
		// set Depth resolution here only once... since no other mode for kinect is available -> deactivating setDepthResolution method!
	//	setDepthOutputMode (getDefaultDepthMode ());
	//	setImageOutputMode (getDefaultImageMode ());

		XnMapOutputMode imageOutputMode;
		imageOutputMode.nXRes = 640;
		imageOutputMode.nYRes = 480;
		imageOutputMode.nFPS = 30;
		CHECK_RC(imageGenerator.SetMapOutputMode(imageOutputMode), "imageGenerator.SetMapOutputMode(imageOutputMode)");

		unsigned short vendorId;
		unsigned short productId;
		unsigned char bus;
		unsigned char address;

		const XnChar* creationInfoXn = deviceInfo.GetCreationInfo();
		std::string cleanId = std::string(creationInfoXn); // "045e/02ae@36/6"

		printf("creationInfoXn=[%s]\n", creationInfoXn);
		std::cout << "creationInfo: " << cleanId << std::endl;

		std::string escapeStr("_");
		cleanId.replace(4, 1, escapeStr); // "045e_02ae@36/6"
		cleanId.replace(9, 1, escapeStr); // "045e_02ae_36/6"
		cleanId.replace(12, 1, escapeStr); // "045e_02ae_36_6"
		std::cout << "cleanId: " << cleanId << std::endl;

		sscanf(deviceInfo.GetCreationInfo(), "%hx/%hx@%hhu/%hhu", &vendorId, &productId, &bus, &address);

		Cam* newCam = new Cam(imageGenerator, cleanId, vendorId, productId, bus, address);
		cams.push_back(newCam);
//		NiDevice* device = new NiDevice(deviceInfo, imageInfo, imageGenerator, this->imageSaver);
//		device->printToString();
//		device->init();
//		this->devices.push_back(device);
	}

	this->dispatchEvent(cams);
	LOG->debug("run() ... THREAD ended");
}
/*private*/ void CamInitializer::dispatchEvent(std::vector<Cam*>& cams) {
	for(int i=0, n=this->listeners.size(); i < n; i++) {
		CamInitializerListener* listener = this->listeners.at(i);
		listener->onInitializedCams(cams);
	}
}

}
