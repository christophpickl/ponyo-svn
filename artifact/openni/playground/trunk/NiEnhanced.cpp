#include <stdio.h>
#include <iostream>
//#include <sstream>
#include "NiEnhanced.h"

using namespace std;

// TODO throw an exception with ERROR CODE only (map to java afterwards with proper message!)
#define THROW_XN_EXCEPTION(errorMessage, returnCode) \
string ss;                                            \
ss.append(errorMessage);                              \
ss.append(": ");                                      \
ss.append(xnGetStatusString(returnCode));             \
throw ss;

NiEnhanced::NiEnhanced() {
	printf("new NiEnhanced()\n");
}

NiEnhanced::~NiEnhanced() {
	printf("~NiEnhanced()\n");
}

void NiEnhanced::initFromXml(string xmlPath) {
	printf("NiEnhanced.initByXml(xmlPath);\n");
	XnStatus returnCode;

	printf("Initializing context ...\n");
	returnCode = this->context.Init();
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Initialisation failed!", returnCode); }

	// -------------------------------------------------------
	cout << "Loading configuration from XML: " << xmlPath << endl;
	xn::EnumerationErrors errors;
	returnCode = this->context.InitFromXmlFile(xmlPath.c_str(), &errors);
	if(returnCode == XN_STATUS_NO_NODE_PRESENT) {
		XnChar errorsToStringBuffer[1024];
		errors.ToString(errorsToStringBuffer, 1024);
		string s;
		s.append(errorsToStringBuffer);
		throw s;
	} else if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Loading config from XML failed!", returnCode) };

	// -------------------------------------------------------
	cout << "Checking configuration for existing nodes ..." << endl;
	returnCode = this->context.FindExistingNode(XN_NODE_TYPE_DEPTH, this->depthGenerator);
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Depth generator not found!", returnCode); }

	returnCode = this->context.FindExistingNode(XN_NODE_TYPE_USER, this->userGenerator);
	if(returnCode != XN_STATUS_OK) { // fallback
		cout << "Could not find user generator node. Try to create one ..." << endl;
		returnCode = this->userGenerator.Create(this->context);
		if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("User generator not found and could not be created!", returnCode); }
	}

	if (!this->userGenerator.IsCapabilitySupported(XN_CAPABILITY_SKELETON)) {
		string errorMsg = "Configured user generator doesn't support skeleton!";
		throw errorMsg;
	}

	// -------------------------------------------------------
	cout << "Registering callbacks ..." << endl;

	XnCallbackHandle TODO_What_todo_with_this_userCallbacks;
	returnCode = this->userGenerator.RegisterUserCallbacks(&NiEnhanced::onNewUser, &NiEnhanced::onUserLost, this, TODO_What_todo_with_this_userCallbacks);
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Registering user callbacks failed!", returnCode); }

	XnCallbackHandle TODO_What_todo_with_this_calibrationCallbacks;
	returnCode = this->userGenerator.GetSkeletonCap().RegisterCalibrationCallbacks(&NiEnhanced::onCalibrationStarted, &NiEnhanced::onCalibrationEnded, this, TODO_What_todo_with_this_calibrationCallbacks);
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Registering calibration callbacks failed!", returnCode); }

	// -------------------------------------------------------
	cout << "Checking for pose capabilities ..." << endl;


	if (this->userGenerator.GetSkeletonCap().NeedPoseForCalibration()) {
		if (!this->userGenerator.IsCapabilitySupported(XN_CAPABILITY_POSE_DETECTION)) {
			string errorMsg = "A pose is required but not supported by the configured hardware!";
			throw errorMsg;
		}
		cout << "Registering additional pose callbacks ..." << endl;
		XnCallbackHandle TODO_What_todo_with_this_poseCallbacks;
		this->userGenerator.GetPoseDetectionCap().RegisterToPoseCallbacks(&NiEnhanced::onPoseDetected, NULL/*onPoseDetectedEnd*/, this, TODO_What_todo_with_this_poseCallbacks);
		this->userGenerator.GetSkeletonCap().GetCalibrationPose(this->requiredPoseName);
		cout << "Calibration pose name: " << this->requiredPoseName << endl;
	}

	this->userGenerator.GetSkeletonCap().SetSkeletonProfile(XN_SKEL_PROFILE_ALL);

	//this->userGenerator.GetSkeletonCap().SetSmoothing(0.8);
	//xnSetMirror(depth, xxMirrorMode);

	// -------------------------------------------------------
	cout << "Start and generating all ..." << endl;

	// "Data Generators do not actually produce any data until specifically asked to do so."
	returnCode = this->context.StartGeneratingAll();
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Could not start generating!", returnCode); }

}
/*static*/ void XN_CALLBACK_TYPE NiEnhanced::onNewUser(xn::UserGenerator& generator, XnUserID userId, void* cookie) {
	printf("onNewUser(generator, userId=%d, cookie)\n", userId);

	NiEnhanced* tthis = static_cast<NiEnhanced*>(cookie);

	printf("Requesting calibration for new user.");
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
}
/*static*/ void XN_CALLBACK_TYPE NiEnhanced::onUserLost(xn::UserGenerator& generator, XnUserID userId, void* cookie) {
	printf("onUserLost(generator, userId=%d, cookie)\n", userId);
}

/*static*/ void XN_CALLBACK_TYPE NiEnhanced::onPoseDetected(xn::PoseDetectionCapability& capability, const XnChar* detectedPoseName, XnUserID userId, void* cookie) {
	printf("Pose %s detected for user %d\n", detectedPoseName, userId);

	NiEnhanced* tthis = static_cast<NiEnhanced*>(cookie);
	tthis->userGenerator.GetPoseDetectionCap().StopPoseDetection(userId);
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
}


/*static*/ void XN_CALLBACK_TYPE NiEnhanced::onCalibrationStarted(xn::SkeletonCapability& capability, XnUserID userId, void* cookie) {
	printf("Calibration started for user %d\n", userId);
}

/*static*/ void XN_CALLBACK_TYPE NiEnhanced::onCalibrationEnded(xn::SkeletonCapability& capability, XnUserID userId, XnBool successfullyCalibrated, void* cookie) {
	NiEnhanced* tthis = static_cast<NiEnhanced*>(cookie);

	if (successfullyCalibrated) {
		printf("Calibration complete, start tracking user %d\n", userId);
		tthis->userGenerator.GetSkeletonCap().StartTracking(userId);
	} else {
		printf("Calibration failed for user %d\n", userId);
		tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
	}
}

/*
Reading Data
 Data Generators constantly receive new data. However, the application may still be using older data (for example, the previous frame of the depth map). As a result of this, any generator should internally store new data, until explicitly requested to update to the newest available data.
 This means that Data Generators "hide" new data internally, until explicitly requested to expose the most updated data to the application, using the UpdateData request function. OpenNI enables the application to wait for new data to be available, and then update it using the xn::Generator::WaitAndUpdateData() function.
 In certain cases, the application holds more than one node, and wants all the nodes to be updated. OpenNI provides several functions to do this, according to the specifications of what should occur before the UpdateData occurs:
 xn::Context::WaitAnyUpdateAll(): Waits for any node to have new data. Once new data is available from any node, all nodes are updated.
 xn::Context::WaitOneUpdateAll(): Waits for a specific node to have new data. Once new data is available from this node, all nodes are updated. This is especially useful when several nodes are producing data, but only one determines the progress of the application.
 xn::Context::WaitNoneUpdateAll(): Does not wait for anything. All nodes are immediately updated.
 xn::Context::WaitAndUpdateAll(): Waits for all nodes to have new data available, and then updates them.
*/
void NiEnhanced::waitAndUpdateAll() {
//	printf("waitAndUpdateAll() ...\n");
	this->context.WaitAndUpdateAll();
//	printf("waitAndUpdateAll() ... TRIGGERED!\n");
}


void NiEnhanced::close() {
	printf("NiEnhanced.close()\n");
	// if(xyGenerator.isGenerating()) xyGenerator.StopGenerating() ... necessary? NO: "The application may also want to stop the data generation without destroying the node, in order to store the configuration, and can do this using the xn::Generator::StopGenerating function."
	this->context.Shutdown();
}
