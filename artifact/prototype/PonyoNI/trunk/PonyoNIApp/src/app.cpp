#include <stdio.h>
#include <stdlib.h> // exit()

#include "common.hpp"
#include "pninclude_opencv.h"
#include "MainWindow.hpp"
#include "OpenNiManager.hpp"

#define IMG_BLUE_TEMPLATE "images/blue_ball_template.jpg"
#define IMG_RED_TEMPLATE "images/red_ball_template.jpg"
#define IMG_TEMPLATE IMG_BLUE_TEMPLATE

#define CAM_ENABLE_IMAGE_GENERATOR true
#define CAM_ENABLE_DEPTH_GENERATOR true
#define CAM_ENABLE_USER_GENERATOR false
#define IMAGE_OUTPUT_MODE_WIDTH XN_VGA_X_RES
#define IMAGE_OUTPUT_MODE_HEIGHT XN_VGA_Y_RES
#define IMAGE_OUTPUT_MODE_FPS 30
// for image generator only: XN_SXGA_RES 1280x1024 + FPS 15

/*

l ... load devices
s ... start generating
c ... calibrate
q ... quit

 */
using namespace pn;

Log* LOG_APP = NEW_LOG(__FILE__)

class App : public MainWindowListener {
public:
	App(const char* pTemplateImagePath) : templateImagePath(pTemplateImagePath) {
		LOG_APP->debug("new App(templateImagePath)");

		this->imageOutputMode.nXRes = IMAGE_OUTPUT_MODE_WIDTH;
		this->imageOutputMode.nYRes = IMAGE_OUTPUT_MODE_HEIGHT;
		this->imageOutputMode.nFPS = IMAGE_OUTPUT_MODE_FPS;

		this->initDescriptor = new CamInitDescriptor(
			CAM_ENABLE_IMAGE_GENERATOR, CAM_ENABLE_DEPTH_GENERATOR, CAM_ENABLE_USER_GENERATOR, this->imageOutputMode);
	}

	~App() {
		LOG_APP->debug("~App()");

		this->window->removeListener(this);

		delete this->window;
		delete this->manager;

		delete this->camInitializer;
		delete this->userManager;

		delete this->camCalibrator;
		delete this->imageSaver;
		delete this->imageConverter;
		delete this->imageDetector;
		delete this->templateImage;
	}

	void main(int argc, char** argv) {
		LOG_APP->info("main()");

		this->initAndWireObjects();
		if(this->manager == NULL) { fprintf(stderr, "This happens when initAndWireObjects() was not invoked before ;)"); return; }

		LOG_APP->trace(">> this->window->init(argc, argv);");
		this->window->init(argc, argv);
		LOG_APP->trace(">> this->manager->init();");
		this->manager->init();
		LOG_APP->trace(">> this->window->display();");
		this->window->display();
	}

	void onListDevices() {
		LOG_APP->info("onListDevices()");

		try {
			this->manager->listDevices(this->initDescriptor);
		} catch (Exception& e) {
			fprintf(stderr, "Exception was thrown while listing devices: %s\n", e.getMessage());
			e.printBacktrace();
		}
	}

	void onStartGenerating() {
		LOG_APP->info("onStartGenerating()");

		try {
			this->manager->startAll();
		} catch (Exception& e) {
			fprintf(stderr, "Exception was thrown while starting up: %s\n", e.getMessage());
			e.printBacktrace();
		}
	}

	void onCalibrate() {
		LOG_APP->info("onCalibrate()");
		try {
			this->manager->calibrate();
		} catch (Exception& e) {
			fprintf(stderr, "Exception was thrown while calibrating: %s\n", e.getMessage());
			e.printBacktrace();
		}
	}

	void onQuit() {
		LOG_APP->info("onQuit()");
		this->manager->shutdown();
		exit(0);
	}

private:
	/** configuration value */
	XnMapOutputMode imageOutputMode;
	/** configuration value */
	CamInitDescriptor* initDescriptor;

	MainWindow* window;
	OpenNiManager* manager;

	CamInitializer* camInitializer;
	UserManager* userManager;

	CamCalibrator* camCalibrator;
	ImageDetector* imageDetector;
	ImageConverter * imageConverter;
	ImageSaver* imageSaver;
	cv::Mat* templateImage;
	const char* templateImagePath;

	void initAndWireObjects() {
		LOG_APP->debug("initAndWireObjects()");
		this->templateImage = new cv::Mat(cvLoadImage(this->templateImagePath)); // TODO store PImage* and cvReleaseImage afterwards?

		// wire objects
		this->imageDetector = new ImageDetector();
		this->imageConverter = new ImageConverter();
		this->imageSaver = new ImageSaver(this->imageConverter);
		this->camCalibrator = new CamCalibrator(this->imageDetector, this->imageConverter, this->imageSaver, this->templateImage);

		this->userManager = new UserManager();
		this->camInitializer = new CamInitializer(this->userManager);
		this->manager = new OpenNiManager(this->camInitializer, this->camCalibrator, this->userManager);

		this->window = new MainWindow();
		this->window->addListener(this);
	}
};


void playground() {
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

int main(int argc, char** argv) {
	println("main() START");

	playground();
//	App app(IMG_TEMPLATE);
//	app.main(argc, argv);

	println("main() END");
	return 0;
}

