#include "common.hpp"
#include "pninclude_opencv.h"
#include "MainWindow.hpp"
#include "OpenNiManager.hpp"

#define IMG_BLUE_TEMPLATE "images/blue_ball_template.jpg"
#define IMG_RED_TEMPLATE "images/red_ball_template.jpg"
#define IMG_TEMPLATE IMG_BLUE_TEMPLATE

#define CAM_ENABLE_IMAGE_GENERATOR true
#define CAM_ENABLE_DEPTH_GENERATOR false
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

xn::Context ctx;
xn::UserGenerator userGenerator;
xn::DepthGenerator depthGenerator;
xn::DepthMetaData tempDepthMetaData;
bool shouldTerminate = false;
XnMapOutputMode mapMode;

void XN_CALLBACK_TYPE onUserNew(xn::UserGenerator& generator, XnUserID userId, void* cookie) {
	printf("onUserNew: %d (requesting calibration)\n", userId);
	userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
}
void XN_CALLBACK_TYPE onUserLost(xn::UserGenerator& generator, XnUserID userId, void* cookie) {
	printf("onUserLost: %d\n", userId);
}
void XN_CALLBACK_TYPE onPoseDetected(xn::PoseDetectionCapability& capability, const XnChar* strPose, XnUserID userId, void* cookie) {
	printf("Pose %s detected for user %d (stopping pose detection, requesting calibration)\n", strPose, userId);
	userGenerator.GetPoseDetectionCap().StopPoseDetection(userId);
	userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
}
void XN_CALLBACK_TYPE onCalibrationStart(xn::SkeletonCapability& capability, XnUserID userId, void* cookie) {
	printf("Calibration started for user %d\n", userId);
}
void XN_CALLBACK_TYPE onCalibrationEnd(xn::SkeletonCapability& capability, XnUserID userId, XnBool calibartionSuccessful, void* cookie) {
	if(calibartionSuccessful) {
		printf("Calibration complete, start tracking user %d\n", userId);
		userGenerator.GetSkeletonCap().StartTracking(userId);
	} else {
		printf("Calibration failed for user %d (again requesting calibration)\n", userId);
		userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
	}
}

void onSignalReceived(int signalCode) {
	printf("onSignalReceived(signalCode=%d)\n", signalCode);
	shouldTerminate = true;
}
void foo() {
	printf("foo()\n");
	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)

	mapMode.nXRes = XN_VGA_X_RES;
	mapMode.nYRes = XN_VGA_Y_RES;
	mapMode.nFPS = 30;

	CHECK_RC(ctx.Init(), "init");
	CHECK_RC(depthGenerator.Create(ctx), "create depth");
	depthGenerator.SetMapOutputMode(mapMode);

	XnStatus userAvailable = ctx.FindExistingNode(XN_NODE_TYPE_USER, userGenerator);
	if(userAvailable != XN_STATUS_OK) {
		CHECK_RC(userGenerator.Create(ctx), "create user");
	}

	XnCallbackHandle hUserCallbacks, hCalibrationCallbacks, hPoseCallbacks;
	xn::SkeletonCapability skel = userGenerator.GetSkeletonCap();
	CHECK_RC(userGenerator.RegisterUserCallbacks(onUserNew, onUserLost, NULL, hUserCallbacks), "register user");
	CHECK_RC(skel.RegisterCalibrationCallbacks(onCalibrationStart, onCalibrationEnd, NULL, hCalibrationCallbacks), "register calib");
	CHECK_RC(userGenerator.GetPoseDetectionCap().RegisterToPoseCallbacks(onPoseDetected, NULL, NULL, hPoseCallbacks), "register pose");

	XnChar poseName[20] = "";
	CHECK_RC(skel.GetCalibrationPose(poseName), "get posename");
	printf("poseName: %s\n", poseName);
	CHECK_RC(skel.SetSkeletonProfile(XN_SKEL_PROFILE_ALL), "set skel profile");
	CHECK_RC(skel.SetSmoothing(0.8), "set smoothing");
//	xnSetMirror(depth, !mirrorMode);

	CHECK_RC(ctx.StartGeneratingAll(), "start generating");

	printf("waitAnyUpdateAll ...\n");
	while(!shouldTerminate) {
		ctx.WaitAnyUpdateAll();
		depthGenerator.GetMetaData(tempDepthMetaData);

		const XnUInt16 userCount = userGenerator.GetNumberOfUsers();
//		printf("userCount: %i\n", userCount);
		XnUserID aUsers[userCount];
		XnUInt16 nUsers = userCount;
		userGenerator.GetUsers(aUsers, nUsers);
		for (int i = 0; i < nUsers; ++i) {
			XnUserID currentUserId = aUsers[i];
			if (userGenerator.GetSkeletonCap().IsTracking(aUsers[i])) {
				XnSkeletonJointPosition joint;
				skel.GetSkeletonJointPosition(currentUserId, XN_SKEL_HEAD, joint);
				XnFloat x = joint.position.X;
				XnFloat y = joint.position.Y;
				XnFloat z = joint.position.Z;
				printf("joint position: %.2f x %.2f x %.2f\n", x, y, z);
				printf("joint.fConfidence: %.2f\n", joint.fConfidence);
			}
		}
	}
	printf("STOP\n");
	CHECK_RC(ctx.StopGeneratingAll(), "stop generating");
	ctx.Shutdown();
}

int main(int argc, char** argv) {
	println("main() START");

	initOpenniLogging();

//	foo();
//	App app(IMG_TEMPLATE);
//	app.main(argc, argv);

	println("main() END");
	return 0;
}

