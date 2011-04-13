#include <stdio.h>
#include <XnCppWrapper.h>

#define TRY(xnStatus) \
	if(xnStatus != XN_STATUS_OK) { \
		fprintf(stderr, "Error: %s\n", xnGetStatusString(xnStatus)); \
		exit(1); \
	}
#define TRY2(xnStatus, msg) \
	if(xnStatus != XN_STATUS_OK) { \
		fprintf(stderr, "Message: %s\nError: %s\n", msg, xnGetStatusString(xnStatus)); \
		exit(1); \
	}

using namespace xn;

bool g_shouldQuit = false;
char g_poseName[80];

void onSignalReceived(int signalCode) {
	g_shouldQuit = true;
}

void onUserNew(UserGenerator& userGenerator, XnUserID userId, void* cookie) {
	printf("onUserNew(%i) ... requesting POSE\n", userId);
//	userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
	userGenerator.GetPoseDetectionCap().StartPoseDetection(g_poseName, userId);
}
void onUserLost(UserGenerator& userGenerator, XnUserID userId, void* cookie) {
	printf("onUserLost(%i)\n", userId);
}
void onPoseStart(PoseDetectionCapability& pose, const XnChar* strPose, XnUserID userId, void* cookie) {
	printf("onPoseStart(...) => request CALIBRATION\n");

	UserGenerator* userGenerator = static_cast<UserGenerator*>(cookie);
	userGenerator->GetPoseDetectionCap().StopPoseDetection(userId);
	userGenerator->GetSkeletonCap().RequestCalibration(userId, TRUE);
}
void onPoseEnd(PoseDetectionCapability& pose, const XnChar* strPose, XnUserID userId, void* cookie) {
	printf("onPoseEnd(...)\n");
}
void onCalibrationStart(SkeletonCapability& skeleton, XnUserID userId, void* cookie) {
	printf("onCalibrationStart(...)\n");
}
void onCalibrationEnd(SkeletonCapability& skeleton, XnUserID userId, XnBool successfullyCalibrated, void* cookie) {
	printf("onCalibrationEnd(..., successfullyCalibrated=%i, ...) => start TRACKING\n", successfullyCalibrated);
	UserGenerator* userGenerator = static_cast<UserGenerator*>(cookie);
	if (successfullyCalibrated) {
		userGenerator->GetSkeletonCap().StartTracking(userId);
	} else {
		userGenerator->GetSkeletonCap().RequestCalibration(userId, TRUE);
	}
}

int main() {
	signal(SIGINT, onSignalReceived);
	signal(SIGTERM, onSignalReceived);

	printf("initializing context ...\n");
	Context context;
	TRY(context.Init());
	TRY(context.OpenFileRecording("/myopenni/myoni.oni"));

	printf("creating depth generator ...\n");
	DepthGenerator depthGenerator;
	TRY(depthGenerator.Create(context));

//	printf("preparing image generator ...\n"); ImageGenerator imageGenerator; TRY(imageGenerator.Create(context)); ... no image generator in recording available

	printf("creating user generator ...\n");
	UserGenerator userGenerator;
	XnStatus foundExistingUserGenerator = context.FindExistingNode(XN_NODE_TYPE_USER, userGenerator);
	if(foundExistingUserGenerator != XN_STATUS_OK) {
		TRY(userGenerator.Create(context));
	}

	SkeletonCapability skeletonCapability = userGenerator.GetSkeletonCap();
	TRY(skeletonCapability.GetCalibrationPose(g_poseName));
	printf("Pose required: %i\n; pose name: %s\n", skeletonCapability.NeedPoseForCalibration(), g_poseName);

	XnCallbackHandle userCallbacks, calibrationCallbacks, poseCallbacks;
	TRY(userGenerator.RegisterUserCallbacks(&onUserNew, &onUserLost, NULL, userCallbacks));
	TRY(skeletonCapability.RegisterCalibrationCallbacks(&onCalibrationStart, &onCalibrationEnd, &userGenerator, calibrationCallbacks));
	TRY(userGenerator.GetPoseDetectionCap().RegisterToPoseCallbacks(&onPoseStart, &onPoseEnd, &userGenerator, poseCallbacks));

	{
		NodeInfoList existingNodes;
		TRY(context.EnumerateExistingNodes(existingNodes));
		for(NodeInfoList::Iterator it = existingNodes.Begin(); it != existingNodes.End(); it++) {
			NodeInfo info = *it;
			printf("info:\n\tcreation info:%s\n\tinstance name: %s\n\tdescr type: %i\n\tdescr name: %s\n\tdescr vendor: %s\n\n",
					info.GetCreationInfo(), info.GetInstanceName(),
					info.GetDescription().Type, info.GetDescription().strName, info.GetDescription().strVendor);
		}
		printf("Joint Availability\n\tHEAD: %i\n\tWAIST: %i\n\tANKLE L: %i\n\tFINGERTIP L: %i\n",
				skeletonCapability.IsJointAvailable(XN_SKEL_HEAD),
				skeletonCapability.IsJointAvailable(XN_SKEL_WAIST),
				skeletonCapability.IsJointAvailable(XN_SKEL_LEFT_ANKLE),
				skeletonCapability.IsJointAvailable(XN_SKEL_LEFT_FINGERTIP));
	}

	DepthMetaData depthData;
	TRY(context.StartGeneratingAll());

	int iteration = 0;
	while(g_shouldQuit == false) {
		TRY(context.WaitAnyUpdateAll());
		depthGenerator.GetMetaData(depthData);

		if(++iteration == 100) {
			iteration = 0;
			XnUInt16 usersCount = userGenerator.GetNumberOfUsers();
			printf("received data! (usersCount=%i)\n", usersCount);
			XnUserID usersArray[usersCount];
			TRY(userGenerator.GetUsers(usersArray, usersCount));
			for(int i = 0; i < usersCount; i++) {
				XnUserID currentId = usersArray[i];
				printf("\tcurrent user id: %i (is tracking: %i)\n", currentId, skeletonCapability.IsTracking(currentId));
				if(skeletonCapability.IsTracking(currentId) == false) {
					continue;
				}
				XnSkeletonJoint jointEnum = XN_SKEL_TORSO;
				printf("\tjoint '%i' is available: %i\n", jointEnum, skeletonCapability.IsJointAvailable(jointEnum));
				if(skeletonCapability.IsJointAvailable(jointEnum) == false) {
					printf("\t\tHead joint not available.");
					continue;
				}
//				printf("JA!!\n");
				XnSkeletonJointPosition jointPosition;
//				XnStatus xns = skeletonCapability.GetSkeletonJointPosition(currentId, jointEnum, jointPosition);
				TRY2(skeletonCapability.GetSkeletonJointPosition(currentId, jointEnum, jointPosition), "Could not get joint position");
				printf("\t\tHead Position (%f): %f x %f x %f\n", jointPosition.fConfidence, jointPosition.position.X, jointPosition.position.Y, jointPosition.position.Z);

//				XnSkeletonJointTransformation jointTransformation;
//				TRY(skeletonCapability.GetSkeletonJoint(currentId, jointEnum, jointTransformation));
//				printf("Head Position (%f): %f x %f x %f\n", jointTransformation.position.fConfidence, jointTransformation.position.position.X, jointTransformation.position.position.Y, jointTransformation.position.position.Z);
//				printf("Head Orientation (%f): (%f x %f x %f) / (%f x %f x %f) / (%f x %f x %f)", jointTransformation.orientation.fConfidence,
//						jointTransformation.orientation.orientation.elements[0], jointTransformation.orientation.orientation.elements[1], jointTransformation.orientation.orientation.elements[2],
//						jointTransformation.orientation.orientation.elements[3], jointTransformation.orientation.orientation.elements[4], jointTransformation.orientation.orientation.elements[5],
//						jointTransformation.orientation.orientation.elements[6], jointTransformation.orientation.orientation.elements[7], jointTransformation.orientation.orientation.elements[8]
//				);
			}
		}
	}

	printf("shutting down ...");
//	TRY(context.StopGeneratingAll()); // TODO Error: The node is locked for changes!
	context.Shutdown();
	return 0;
}
