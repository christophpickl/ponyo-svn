#include <ponyo/openni/UserManager.hpp>

namespace pn {

Log* UserManager::LOG = NEW_LOG();

UserManager::UserManager() : skeletonCapability(NULL) {
	LOG->debug("new UserManager()");
}

/*public*/ UserManager::~UserManager() {
	LOG->debug("~UserManager()");
	this->unregister(); // just to be sure, invoke in any case
}
/*public*/ void UserManager::unregister() {
	LOG->debug("unregister()");

	this->userGenerator.UnregisterUserCallbacks(this->callbackUser);
	this->userGenerator.GetSkeletonCap().UnregisterCalibrationCallbacks(this->callbackCalibration);
	this->userGenerator.GetPoseDetectionCap().UnregisterFromPoseCallbacks(this->callbackPose);
}

/*public*/ void UserManager::init(xn::Context& context) throw(UserManagerException, OpenNiException) {
	LOG->debug("init(context) START");

	// -------------------------------------------------------
	LOG->debug("Setting up user generator node.");

	const XnStatus foundExistingUserGenerator = context.FindExistingNode(XN_NODE_TYPE_USER, this->userGenerator);
	if(foundExistingUserGenerator != XN_STATUS_OK) {
		LOG->debug("Not found existing UserGenerator; going to create one.");
		XNTRY(this->userGenerator.Create(context), "userGenerator.Create(context)");
	}

	if(!this->userGenerator.IsCapabilitySupported(XN_CAPABILITY_SKELETON)) {
		throw UserManagerException("Supplied user generator doesn't support skeleton", AT);
	}
	this->skeletonCapability = this->userGenerator.GetSkeletonCap();

	// -------------------------------------------------------
	LOG->debug("Registering callbacks.");

	XNTRY(this->userGenerator.RegisterUserCallbacks(&UserManager::onUserNew, &UserManager::onUserLost, this, this->callbackUser),
		"Registering user callbacks for user generator failed!");

	XNTRY(this->skeletonCapability.RegisterCalibrationCallbacks(&UserManager::onCalibrationStart, &UserManager::onCalibrationEnd, this, this->callbackCalibration),
		"Registering calibration callbacks for user generator failed!");

	this->poseRequired = this->skeletonCapability.NeedPoseForCalibration() == TRUE;
	if(this->poseRequired) {
		if(!this->userGenerator.IsCapabilitySupported(XN_CAPABILITY_POSE_DETECTION)) {
			throw UserManagerException("A pose is required but not supported by your hardware!", AT);
		}

		this->userGenerator.GetPoseDetectionCap().RegisterToPoseCallbacks(&UserManager::onPoseDetected, NULL/*onPoseDetectedEnd*/, this, this->callbackPose);
		this->skeletonCapability.GetCalibrationPose(this->requiredPoseName);

//		printf("Required pose name: '%s'\n", this->requiredPoseName);
		std::cout << "Calibration pose name: '" << this->requiredPoseName << "'" << std::endl;
	} else {
		LOG->debug("No calibration pose required.");
	}

	this->skeletonCapability.SetSkeletonProfile(XN_SKEL_PROFILE_ALL); //  we want to have all joints
	// XNTRY(this->skeletonCapability.SetSmoothing(0.0f), "Setting smoothing for skeleton capability failed!");

	this->userGenerator.StartGenerating();

	LOG->debug("init(context) END");
}


/*public*/ void UserManager::update() throw(OpenNiException) {
	const XnUInt16 constUserCount = this->userGenerator.GetNumberOfUsers();
	XnUserID userIds[constUserCount];
	XnUInt16 userCount = constUserCount;
	XNTRY(this->userGenerator.GetUsers(userIds, userCount), "Getting users failed!");
	for (int i = 0; i < userCount; ++i) {
		XnUserID currentUserId = userIds[i];
		if (!this->skeletonCapability.IsTracking(currentUserId)) {
			// user is detected (new), but not yet calibrated; skip. // still, could have checked with IsTracking(), but because of performance reasons...
			continue;
		}
		/*
		this->broadcastJointPositions(currentUserId, XN_SKEL_HEAD, 0);
		this->broadcastJointPositions(currentUserId, XN_SKEL_NECK, 1);
		this->broadcastJointPositions(currentUserId, XN_SKEL_TORSO, 2);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_WAIST, 3);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_COLLAR, 4);
		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_SHOULDER, 5);
		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_ELBOW, 6);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_WRIST, 7);
		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_HAND, 8);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_FINGERTIP, 9);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_COLLAR, 10);
		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_SHOULDER, 11);
		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_ELBOW, 12);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_WRIST, 13);
		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_HAND, 14);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_FINGERTIP, 15);
		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_HIP, 16);
		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_KNEE, 17);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_ANKLE, 18);
		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_FOOT, 19);
		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_HIP, 20);
		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_KNEE, 21);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_ANKLE, 22);
		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_FOOT, 23);
		*/
	}
}

/*private*/ inline void UserManager::broadcastJointPositions(XnUserID currentUserId, XnSkeletonJoint jointEnum, int jointId) {
	XnSkeletonJointPosition jointPosition;

	// TODO strange, this is how it should be ... :-/
//	this->skeletonCapability.GetSkeletonJointPosition(currentUserId, jointEnum, jointPosition);
	XNTRY(this->skeletonCapability.GetSkeletonJointPosition(currentUserId, jointEnum, jointPosition), "Get joint position failed");

	if(jointPosition.fConfidence < 0.5) { return; }
	printf("joint position: %i.X = %f\n", jointEnum, jointPosition.position.X);
//	for(int i=0, n = this->listeners.size(); i < n; i++) {
//		this->listeners.at(i)->onJointPositionChanged(currentUserId, jointId, jointPosition);
//	}
}

///*private*/ inline void UserManager::broadcastUserChangeState(int userId, UserState userState) {
////	for(int i=0, n = this->listeners.size(); i < n; i++) {
////		this->listeners.at(i)->onUserStateChanged(userId, userState);
////	}
//}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onUserNew(xn::UserGenerator& generator, XnUserID/*unsigned int*/ userId, void* cookie) {
	printf("UserManager.onNewUser(generator, userId=%d, cookie)\n", userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);
	printf("Requesting calibration for new user.");
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);

//	tthis->broadcastUserChangeState(userId, USER_STATE_NEW);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onUserLost(xn::UserGenerator& generator, XnUserID userId, void* cookie) {
	printf("UserManager.onUserLost(generator, userId=%d, cookie)\n", userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);
//	tthis->broadcastUserChangeState(userId, USER_STATE_LOST);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationStart(xn::SkeletonCapability& capability, XnUserID userId, void* cookie) {
	printf("Calibration started for user %d\n", userId);
	UserManager* tthis = static_cast<UserManager*>(cookie);
//	tthis->broadcastUserChangeState(userId, USER_STATE_CALIBRATION_STARTED);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationEnd(xn::SkeletonCapability& capability, XnUserID userId, XnBool successfullyCalibrated, void* cookie) {
	UserManager* tthis = static_cast<UserManager*>(cookie);

	if (successfullyCalibrated) {
		printf("Calibration complete, start tracking user %d\n", userId);
		tthis->userGenerator.GetSkeletonCap().StartTracking(userId);
//		tthis->broadcastUserChangeState(userId, USER_STATE_CALIBRATION_ENDED_SUCCESFULLY);
	} else {
		printf("Calibration failed for user %d\n", userId);
		tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
//		tthis->broadcastUserChangeState(userId, USER_STATE_CALIBRATION_ENDED_UNSUCCESFULLY);
	}
}

// TODO when is this invoked??
/*private static*/ void XN_CALLBACK_TYPE UserManager::onPoseDetected(xn::PoseDetectionCapability& capability, const XnChar* detectedPoseName, XnUserID userId, void* cookie) {
	printf("UserManager.Pose %s detected for user %d\n", detectedPoseName, userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);
	tthis->userGenerator.GetPoseDetectionCap().StopPoseDetection(userId);
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
//	tthis->broadcastUserChangeState(userId, USER_STATE_POSE_DETECTED);
}

}
