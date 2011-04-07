#include "CamInitializer.hpp"

namespace pn {

Log* CamInitializer::LOG = NEW_LOG(__FILE__)

CamInitializer::CamInitializer(UserManager* pUserManager) : userManager(pUserManager) {
	LOG->debug("new CamInitializer(userManager)");
}

CamInitializer::~CamInitializer() {
	LOG->debug("~CamInitializer()");
}

void CamInitializer::fetchDevices(xn::Context& context, CamInitDescriptor* initDescriptor) {
	LOG->debug("fetchDevices(context)");

	LOG->trace("fetchDevices # 1/3");
	this->initDescriptor = initDescriptor;
	LOG->trace("fetchDevices # 2/3");
	this->workerThread = boost::thread(&CamInitializer::run, this, context);
	LOG->trace("fetchDevices # 3/3");
}

/**
 * Thread run.
 */
/*private*/ void CamInitializer::run(xn::Context& context) {
	LOG->debug("run() ... THREAD started");

	std::vector<Cam*> cams;

	std::vector<xn::NodeInfo> deviceInfos;
	xn::NodeInfoList deviceInfoList;
	this->loadDeviceInfos(deviceInfos, deviceInfoList, context);
	printf("CamInitializer ... found devices count: %i\n", (int) deviceInfos.size());

	if(!deviceInfos.empty()) {

		// Image Generator
		// -------------------------------------------------------------------
		std::vector<xn::NodeInfo> imageInfos;
		xn::NodeInfoList imageInfoList;
		if(this->initDescriptor->isImageGeneratorRequired()) {
			LOG->trace(">> context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, ..)");
			CHECK_RC(context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, NULL,imageInfoList, NULL),
				"context.EnumerateProductionTrees(IMAGE)");

			for (xn::NodeInfoList::Iterator it = imageInfoList.Begin(); it != imageInfoList.End(); ++it) {
				imageInfos.push_back(*it);
			}

			if(deviceInfos.size() != imageInfos.size() /* || dev.size != depth.size */) {
				LOG->debug("run() ... THREAD aborting");
				OpenNiException ex("Number of devices does not match number of image streams!", AT);
				this->dispatchOnException(ex);
				return;
			}
		}

		// Depth Generator
		// -------------------------------------------------------------------
		//	printf("Looking for available depth generators...\n");
		//	static xn::NodeInfoList depth_nodes;
		//	status = context_.EnumerateProductionTrees (XN_NODE_TYPE_DEPTH, NULL, depth_nodes, NULL);
		//	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Enumerating depth generator nodes failed!", returnCode); }
		//	vector<xn::NodeInfo> depth_info;
		//	for (xn::NodeInfoList::Iterator nodeIt = depth_nodes.Begin (); nodeIt != depth_nodes.End (); ++nodeIt) {
		//		depth_info.push_back (*nodeIt);
		//	}

		// User Generator
		// -------------------------------------------------------------------
		if(this->initDescriptor->isUserGeneratorRequired()) {
			try {
				this->userManager->init(context);
			} catch(Exception& ex) {
				LOG->debug("run() ... THREAD aborting");
				this->dispatchOnException(ex);
				return;
			}
		}

		// Foreach Device => create some nodes and Cam object
		// -------------------------------------------------------------------
		for (unsigned i = 0, n = deviceInfos.size(); i < n; i++) {
			printf("CamInitializer ... Processing device number %i of %i\n", (i+1), n);
			xn::NodeInfo deviceInfo = deviceInfos[i];


		//	returnCode = this->context.CreateProductionTree(const_cast<xn::NodeInfo&>(deviceInfo));
		//	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Creating depth generator failed!", returnCode); }

			xn::ImageGenerator imageGenerator;
			if(this->initDescriptor->isImageGeneratorRequired()) {
				LOG->trace(">> context.CreateProductionTree(imageInfo)");
				xn::NodeInfo imageInfo = imageInfos[i];
				CHECK_RC(context.CreateProductionTree(const_cast<xn::NodeInfo&>(imageInfo)), "Creating image generator instance failed!");

				LOG->trace(">> imageInfo.GetInstance(imageGenerator)");
				CHECK_RC(imageInfo.GetInstance(imageGenerator),
					"Creating image generator instance failed!");
				CHECK_RC(imageGenerator.SetMapOutputMode(this->initDescriptor->getImageOutputMode()),
					"imageGenerator.SetMapOutputMode(imageOutputMode)");
			}

			// get production node instances
		//	returnCode = depth_node.GetInstance(depth_generator_);
		//	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Creating depth generator instance failed!", returnCode); }

			// call virtual function to find available modes specifically for each device type
		//	this->getAvailableModes();
			// set Depth resolution here only once... since no other mode for kinect is available -> deactivating setDepthResolution method!
		//	setDepthOutputMode (getDefaultDepthMode ());
		//	setImageOutputMode (getDefaultImageMode ());

			Cam* newCam = this->createCamInstance(imageGenerator, deviceInfo);
			cams.push_back(newCam);
		}
	}

	this->dispatchOnInitializedCams(cams);
	LOG->fatal("run() ... THREAD ended");
}

/**
 * Device Infos.
 */
/*private*/ void CamInitializer::loadDeviceInfos(std::vector<xn::NodeInfo>& deviceInfos, xn::NodeInfoList& deviceInfoList, xn::Context& context) {
	LOG->debug("loadDeviceInfos(context)");

	LOG->trace(">> context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, ..)");
	XnStatus returnCode = context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, NULL, deviceInfoList); // XnStatus EnumerateProductionTrees(XnProductionNodeType Type, Query* pQuery, NodeInfoList& TreesList, EnumerationErrors* pErrors = NULL) const
	if(returnCode != XN_STATUS_OK) {
		printf("Fetching devices failed (Probably no devices connected?!)\nOpenNI error message: %s\n", xnGetStatusString(returnCode));
		return;
	}

	if(deviceInfoList.Begin() == deviceInfoList.End()) {
		println("Device node list is empty! (Probably no devices connected?!)");
		return;
	}

	for (xn::NodeInfoList::Iterator it = deviceInfoList.Begin(); it != deviceInfoList.End(); ++it) {
		deviceInfos.push_back(*it);
	}
}


/**
 * Cam Factory method.
 */
/*private*/ Cam* CamInitializer::createCamInstance(xn::ImageGenerator& imageGenerator, xn::NodeInfo& deviceInfo) {
	LOG->debug("createCamInstance(..)");

	unsigned short vendorId;
	unsigned short productId;
	unsigned char bus;
	unsigned char address;

	const XnProductionNodeDescription& description = deviceInfo.GetDescription();
//	printf("\tdeviceInfo: description.vendor=%s, description.name=%s\n", description.strVendor, description.strName);

	const XnChar* creationInfoXn = deviceInfo.GetCreationInfo(); // \\?\usb#vid_045e&pid_02ae#a00362....102a#{.(some more numbers)..}
//	printf("CamInitializer ... creationInfoXn=[%s]\n", creationInfoXn);
	std::string cleanId = std::string(creationInfoXn); // "045e/02ae@36/6"
	std::string escapeStr("_");
	cleanId.replace(4, 1, escapeStr); // "045e_02ae@36/6"
	cleanId.replace(9, 1, escapeStr); // "045e_02ae_36/6"
	cleanId.replace(12, 1, escapeStr); // "045e_02ae_36_6"
	// cleanId == "045e_02ae_38_12"
//	printf("XXXXXXXXXXXXXX cleanId: %s\n", cleanId.c_str());

	sscanf(creationInfoXn, "%hx/%hx@%hhu/%hhu", &vendorId, &productId, &bus, &address);

	Cam* newCam = new Cam(imageGenerator, cleanId, this->initDescriptor);// vendorId, productId, bus, address);
	return newCam;
}

/**
 * Dispatcher.
 */
/*private*/ void CamInitializer::dispatchOnInitializedCams(std::vector<Cam*>& cams) {
	for(int i=0, n=this->listeners.size(); i < n; i++) {
		CamInitializerListener* listener = this->listeners.at(i);
		listener->onInitializedCams(cams);
	}
}

/**
 * Dispatcher.
 */
/*private*/ void CamInitializer::dispatchOnException(Exception& ex) {
	for(int i=0, n=this->listeners.size(); i < n; i++) {
		CamInitializerListener* listener = this->listeners.at(i);
		listener->onException(ex);
	}
}

}
