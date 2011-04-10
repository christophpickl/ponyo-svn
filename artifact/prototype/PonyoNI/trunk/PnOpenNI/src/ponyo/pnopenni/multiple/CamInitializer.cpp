#include <ponyo/pnopenni/multiple/CamInitializer.hpp>

namespace pn {

Log* CamInitializer::LOG = NEW_LOG();

CamInitializer::CamInitializer(UserManager* pUserManager) :
	userManager(pUserManager) {
	LOG->debug("new CamInitializer(userManager)");
}

CamInitializer::~CamInitializer() {
	LOG->debug("~CamInitializer()");
}

void CamInitializer::fetchDevices(xn::Context& context,
		CamInitDescriptor* initDescriptor) {
	LOG->debug("fetchDevices(context)");

	this->initDescriptor = initDescriptor;
	this->workerThread = boost::thread(&CamInitializer::run, this, context);
}

/**
 * Thread run.
 */
/*private*/void CamInitializer::run(xn::Context& context) {
	LOG->debug("run() ... THREAD started");

	std::vector<Cam*> cams;

	std::vector<xn::NodeInfo> deviceInfos;
	static xn::NodeInfoList deviceInfoList;
	this->loadDeviceInfos(deviceInfos, deviceInfoList, context);

	if (!deviceInfos.empty()) {

		// Enumerate Depth Generators
		// -------------------------------------------------------------------
		std::vector<xn::NodeInfo> depthInfos;
		static xn::NodeInfoList depthInfoList;

		if (this->initDescriptor->isDepthGeneratorRequired()) {
			LOG->trace(
					">> context.EnumerateProductionTrees(XN_NODE_TYPE_DEPTH, ..)");
			CHECK_RC(context.EnumerateProductionTrees(XN_NODE_TYPE_DEPTH, NULL, depthInfoList, NULL), "context.EnumerateProductionTrees(DEPTH)");

			for (xn::NodeInfoList::Iterator it = depthInfoList.Begin(); it
					!= depthInfoList.End(); ++it) {
				printf("Found DEPTH name: '%s'\n",
						(*it).GetDescription().strName); // GetCreationInfo() == ""
				depthInfos.push_back(*it);
			}

			if (deviceInfos.size() != depthInfos.size()) {
				LOG->debug("run() ... THREAD aborting");
				OpenNiException
						ex(
								"Number of devices does not match number of depth streams!",
								AT);
				this->dispatchOnException(ex);
				return;
			}
		}

		// Enumerate Image Generators
		// -------------------------------------------------------------------
		std::vector<xn::NodeInfo> imageInfos;
		static xn::NodeInfoList imageInfoList;

		if (this->initDescriptor->isImageGeneratorRequired()) {
			LOG->trace(
					">> context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, ..)");
			CHECK_RC(context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, NULL, imageInfoList, NULL), "context.EnumerateProductionTrees(IMAGE)");

			for (xn::NodeInfoList::Iterator it = imageInfoList.Begin(); it
					!= imageInfoList.End(); ++it) {
				printf("Found IMAGE name: '%s'\n",
						(*it).GetDescription().strName); // GetCreationInfo() == ""
				imageInfos.push_back(*it);
			}

			if (deviceInfos.size() != imageInfos.size()) {
				LOG->debug("run() ... THREAD aborting");
				OpenNiException
						ex(
								"Number of devices does not match number of image streams!",
								AT);
				this->dispatchOnException(ex);
				return;
			}
		}

		// User Generator
		// -------------------------------------------------------------------
		if (this->initDescriptor->isUserGeneratorRequired()) {
			try {
				this->userManager->init(context);
			} catch (Exception& ex) {
				LOG->debug("run() ... THREAD aborting");
				this->dispatchOnException(ex);
				return;
			}
		}

		// Foreach Device => create some nodes and Cam object
		// ===================================================================
		for (unsigned i = 0, n = deviceInfos.size(); i < n; i++) {
			printf("CamInitializer ... Processing device number %i of %i\n",
					(i + 1), n);
			xn::NodeInfo deviceInfo = deviceInfos[i];

			// NO! this->context.CreateProductionTree(const_cast<xn::NodeInfo&>(deviceInfo));
			//			xn::DepthGenerator depthGenerator;
			//			xn::ImageGenerator imageGenerator;
			//			xn::NodeInfo depthInfo = depthInfos[i];
			//			xn::NodeInfo imageInfo = imageInfos[i];
			//
			//			CHECK_RC(context.CreateProductionTree(const_cast<xn::NodeInfo&>(imageInfo)), "Creating image generator instance failed!");
			//			CHECK_RC(context.CreateProductionTree(const_cast<xn::NodeInfo&>(depthInfo)), "Creating depth generator instance failed!");
			//
			//			CHECK_RC(depthInfo.GetInstance(depthGenerator), "Getting depth production node instance failed!");
			//			CHECK_RC(imageInfo.GetInstance(imageGenerator), "Creating image generator instance failed!");


			// Depth Generator
			// -------------------------------------------------------------------
			xn::DepthGenerator depthGenerator;
			if (this->initDescriptor->isDepthGeneratorRequired()) {
				xn::NodeInfo depthInfo = depthInfos[i];

				LOG->trace("Creating DEPTH node ...");
				//				CHECK_RC(context.CreateProductionTree(const_cast<xn::NodeInfo&>(depthInfo)), "Creating depth generator instance failed!");
				CHECK_RC(context.CreateAnyProductionTree(XN_NODE_TYPE_DEPTH, NULL, depthGenerator, NULL), "CreateAnyProductionTree(DEPTH)");

				LOG->trace(">> depthInfo.GetInstance(depthGenerator)");
				CHECK_RC(depthInfo.GetInstance(depthGenerator), "Getting depth production node instance failed!");
				printf("a\n");
				XnMapOutputMode foo;
				foo.nFPS = 30;
				foo.nXRes = 640;
				foo.nYRes = 480;
				//				CHECK_RC(depthGenerator.SetMapOutputMode(foo), "depthGenerator.SetMapOutputMode(..)");
				//				CHECK_RC(depthGenerator.SetMapOutputMode(this->initDescriptor->getMapOutputMode()), "depthGenerator.SetMapOutputMode(..)");
				printf("b\n");
				// 2 ... software/kinect, 1 ... hardware/primesense
				//				CHECK_RC(depthGenerator.SetIntProperty("RegistrationType", 2), "Setting RegistrationType property for depth generator failed!");
				printf("c\n");
			}

			// Image Generator
			// -------------------------------------------------------------------
			xn::ImageGenerator imageGenerator;
			if (this->initDescriptor->isImageGeneratorRequired()) {
				xn::NodeInfo imageInfo = imageInfos[i];

				LOG->trace("Creating IMAGE node ...");
				//				CHECK_RC(context.CreateProductionTree(const_cast<xn::NodeInfo&>(imageInfo)), "Creating image generator instance failed!");
				CHECK_RC(context.CreateAnyProductionTree(XN_NODE_TYPE_IMAGE, NULL, imageGenerator, NULL), "CreateProductionTree(IMAGE)");

				LOG->trace(">> imageInfo.GetInstance(imageGenerator)");
				CHECK_RC(imageInfo.GetInstance(imageGenerator), "Creating image generator instance failed!");
				//				CHECK_RC(imageGenerator.SetMapOutputMode(this->initDescriptor->getMapOutputMode()), "imageGenerator.SetMapOutputMode(..)");
				//				CHECK RC(imageGenerator.SetIntProperty("InputFormat", 6); // set kinect specific format. Thus input = uncompressed Bayer, output = grayscale = bypass = bayer
				//				CHECK RC(imageGenerator.SetPixelFormat(XN_PIXEL_FORMAT_GRAYSCALE_8_BIT); // Grayscale: bypass debayering -> gives us bayer pattern!
			}

			Cam* newCam = this->createCamInstance(imageGenerator, /*depthGenerator, */
					deviceInfo);
			cams.push_back(newCam);
		}
	}

	//this->userGenerator.GetSkeletonCap().SetSmoothing(0.8);
	//xnSetMirror(depth, xxMirrorMode);

	this->dispatchOnInitializedCams(cams);
	LOG->fatal("run() ... THREAD ended");
}

/**
 * Device Infos.
 */
/*private*/void CamInitializer::loadDeviceInfos(
		std::vector<xn::NodeInfo>& deviceInfos,
		xn::NodeInfoList& deviceInfoList, xn::Context& context) {
	LOG->debug("loadDeviceInfos(context)");

	LOG->trace(">> context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE, ..)");
	XnStatus returnCode = context.EnumerateProductionTrees(XN_NODE_TYPE_DEVICE,
			NULL, deviceInfoList); // XnStatus EnumerateProductionTrees(XnProductionNodeType Type, Query* pQuery, NodeInfoList& TreesList, EnumerationErrors* pErrors = NULL) const
	if (returnCode != XN_STATUS_OK) {
		printf(
				"Fetching devices failed (Probably no devices connected?!)\nOpenNI error message: %s\n",
				xnGetStatusString(returnCode));
		return;
	}

	if (deviceInfoList.Begin() == deviceInfoList.End()) {
		println("Device node list is empty! (Probably no devices connected?!)");
		return;
	}

	for (xn::NodeInfoList::Iterator it = deviceInfoList.Begin(); it
			!= deviceInfoList.End(); ++it) {
		// GetDescription().strName == "SensorV2"
		// GetDescription().strVendor == "PrimeSense"
		// GetInstanceName() == ""
		printf("Found DEVICE creation info: '%s'\n", (*it).GetCreationInfo());
		deviceInfos.push_back(*it);
	}
}

/**
 * Cam Factory method.
 */
/*private*/Cam* CamInitializer::createCamInstance(
		xn::ImageGenerator& imageGenerator, xn::NodeInfo& deviceInfo) {
	LOG->debug("createCamInstance(..)");

	unsigned short vendorId;
	unsigned short productId;
	unsigned char bus;
	unsigned char address;

	const XnProductionNodeDescription& description =
			deviceInfo.GetDescription();
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

	sscanf(creationInfoXn, "%hx/%hx@%hhu/%hhu", &vendorId, &productId, &bus,
			&address);

	Cam* newCam = new Cam(imageGenerator, cleanId, this->initDescriptor);// vendorId, productId, bus, address);
	return newCam;
}

/**
 * Dispatcher.
 */
/*private*/void CamInitializer::dispatchOnInitializedCams(
		std::vector<Cam*>& cams) {
	for (int i = 0, n = this->listeners.size(); i < n; i++) {
		CamInitializerListener* listener = this->listeners.at(i);
		listener->onInitializedCams(cams);
	}
}

/**
 * Dispatcher.
 */
/*private*/void CamInitializer::dispatchOnException(Exception& ex) {
	for (int i = 0, n = this->listeners.size(); i < n; i++) {
		CamInitializerListener* listener = this->listeners.at(i);
		listener->onException(ex);
	}
}

}
