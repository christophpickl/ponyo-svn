#include <ponyo/pnopenni/UserManager.hpp>

namespace pn {

Log* UserManager::LOG = NEW_LOG(__FILE__)

UserManager::UserManager() : skeletonCapability(NULL) {
	LOG->debug("new UserManager()");
}

/*public*/ UserManager::~UserManager() {
	LOG->debug("~UserManager() ... unregistering callbacks");

	this->userGenerator.UnregisterUserCallbacks(this->callbackUser);
	this->userGenerator.GetSkeletonCap().UnregisterCalibrationCallbacks(this->callbackCalibration);
	this->userGenerator.GetPoseDetectionCap().UnregisterFromPoseCallbacks(this->callbackPose);

	LOG->debug("~UserManager() END");
}

/*public*/ void UserManager::init(xn::Context& context) throw(UserManagerException, OpenNiException) {
	LOG->debug("init(context) START");

	// -------------------------------------------------------
	LOG->debug("Setting up user generator node.");

	const XnStatus foundExistingUserGenerator = context.FindExistingNode(XN_NODE_TYPE_USER, this->userGenerator);
	if(foundExistingUserGenerator != XN_STATUS_OK) {
		LOG->debug("Not found existing UserGenerator; going to create one.");
		CHECK_RC(this->userGenerator.Create(context), "userGenerator.Create(context)");
	}

	if(!this->userGenerator.IsCapabilitySupported(XN_CAPABILITY_SKELETON)) {
		throw UserManagerException("Supplied user generator doesn't support skeleton", AT);
	}
	this->skeletonCapability = this->userGenerator.GetSkeletonCap();

	// -------------------------------------------------------
	LOG->debug("Registering callbacks.");

	CHECK_RC(this->userGenerator.RegisterUserCallbacks(&UserManager::onUserNew, &UserManager::onUserLost, this, this->callbackUser),
		"Registering user callbacks for user generator failed!");

	CHECK_RC(this->skeletonCapability.RegisterCalibrationCallbacks(&UserManager::onCalibrationStart, &UserManager::onCalibrationEnd, this, this->callbackCalibration),
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
		std::cout << "No calibration pose required." << std::endl;
	}

	this->skeletonCapability.SetSkeletonProfile(XN_SKEL_PROFILE_ALL); //  we want to have all joints

	//this->userGenerator.GetSkeletonCap().SetSmoothing(0.8);
	//xnSetMirror(depth, xxMirrorMode);

	LOG->debug("init(context) END");
}

/*public*/ void UserManager::start() throw(OpenNiException) {
	if(this->userGenerator.IsGenerating()) {
		LOG->warn("start() ... aborting, as already generating!");
		return;
	}

	LOG->info("start()");
	CHECK_RC(this->userGenerator.StartGenerating(), "Starting user generator failed!");
}

/*public*/ bool UserManager::isRunning() {
	return this->userGenerator.IsGenerating() == TRUE ? true : false;
}

/*public*/ void UserManager::stop() throw(OpenNiException) {
	if(!this->userGenerator.IsGenerating()) {
		LOG->warn("stop() ... aborting, as not generating!");
		return;
	}

	LOG->info("stop()");
	CHECK_RC(this->userGenerator.StopGenerating(), "Stopping user generator failed!");
}

/*public*/ void UserManager::update() throw(OpenNiException) {
	const XnUInt16 constUserCount = this->userGenerator.GetNumberOfUsers();
	XnUserID userIds[constUserCount];
	XnUInt16 userCount = constUserCount;
	CHECK_RC(this->userGenerator.GetUsers(userIds, userCount), "Getting users failed!");
	for (int i = 0; i < userCount; ++i) {
		XnUserID currentUserId = userIds[i];
		if (!this->skeletonCapability.IsTracking(currentUserId)) {
			// user is detected (new), but not yet calibrated; skip. // still, could have checked with IsTracking(), but because of performance reasons...
			continue;
		}

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

//		XnSkeletonJointOrientation jointOrientation; NO => if interested in position AND orientation ==> use GetSkeletonJoint() instead!!
//		CHECK_RC(this->skeletonCapability.GetSkeletonJointOrientation(currentUserId, XN_SKEL_HEAD, jointOrientation), "Get joint orientation failed!");
//		if(jointOrientation.fConfidence < 0.5) { continue; }
//		jointOrientation.orientation.elements[0], jointOrientation.orientation.elements[1], jointOrientation.orientation.elements[2],
//		jointOrientation.orientation.elements[3], jointOrientation.orientation.elements[4], jointOrientation.orientation.elements[5],
//		jointOrientation.orientation.elements[6], jointOrientation.orientation.elements[7], jointOrientation.orientation.elements[8],
//		printf("joint update:\n\tposition: %.2f x %.2f x %.2f (confidence: %.2f)\n\torientation: [ (%.2f, %.2f, %.2f) - (%.2f, %.2f, %.2f) - (%.2f, %.2f, %.2f)] (confidence: %.2f)\n",
//				jointPosition.position.X, jointPosition.position.Y, jointPosition.position.Z, jointPosition.fConfidence,
//				jointOrientation.orientation.elements[0], jointOrientation.orientation.elements[1], jointOrientation.orientation.elements[2],
//				jointOrientation.orientation.elements[3], jointOrientation.orientation.elements[4], jointOrientation.orientation.elements[5],
//				jointOrientation.orientation.elements[6], jointOrientation.orientation.elements[7], jointOrientation.orientation.elements[8],
//				jointOrientation.fConfidence);
	}
}



/*private*/ inline void UserManager::broadcastJointPositions(XnUserID currentUserId, XnSkeletonJoint jointEnum, int jointId) {
	XnSkeletonJointPosition jointPosition;

	// TODO strange, this is how it should be ... :-/
//	this->skeletonCapability.GetSkeletonJointPosition(currentUserId, jointEnum, jointPosition);
	CHECK_RC(this->skeletonCapability.GetSkeletonJointPosition(currentUserId, jointEnum, jointPosition), "Get joint position failed");

	if(jointPosition.fConfidence < 0.5) { return; }

	for(int i=0, n = this->listeners.size(); i < n; i++) {
		this->listeners.at(i)->onJointPositionChanged(currentUserId, jointId, jointPosition);
	}
}

/*private*/ inline void UserManager::broadcastUserChangeState(int userId, UserState userState) {
	for(int i=0, n = this->listeners.size(); i < n; i++) {
		this->listeners.at(i)->onUserStateChanged(userId, userState);
	}
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onUserNew(xn::UserGenerator& generator, XnUserID/*unsigned int*/ userId, void* cookie) {
	printf("UserManager.onNewUser(generator, userId=%d, cookie)\n", userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);
	printf("Requesting calibration for new user.");
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);

	tthis->broadcastUserChangeState(userId, USER_STATE_NEW);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onUserLost(xn::UserGenerator& generator, XnUserID userId, void* cookie) {
	printf("UserManager.onUserLost(generator, userId=%d, cookie)\n", userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);
	tthis->broadcastUserChangeState(userId, USER_STATE_LOST);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationStart(xn::SkeletonCapability& capability, XnUserID userId, void* cookie) {
	printf("Calibration started for user %d\n", userId);
	UserManager* tthis = static_cast<UserManager*>(cookie);
	tthis->broadcastUserChangeState(userId, USER_STATE_CALIBRATION_STARTED);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationEnd(xn::SkeletonCapability& capability, XnUserID userId, XnBool successfullyCalibrated, void* cookie) {
	UserManager* tthis = static_cast<UserManager*>(cookie);

	if (successfullyCalibrated) {
		printf("Calibration complete, start tracking user %d\n", userId);
		tthis->userGenerator.GetSkeletonCap().StartTracking(userId);
		tthis->broadcastUserChangeState(userId, USER_STATE_CALIBRATION_ENDED_SUCCESFULLY);
	} else {
		printf("Calibration failed for user %d\n", userId);
		tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
		tthis->broadcastUserChangeState(userId, USER_STATE_CALIBRATION_ENDED_UNSUCCESFULLY);
	}
}

// TODO when is this invoked??
/*private static*/ void XN_CALLBACK_TYPE UserManager::onPoseDetected(xn::PoseDetectionCapability& capability, const XnChar* detectedPoseName, XnUserID userId, void* cookie) {
	printf("UserManager.Pose %s detected for user %d\n", detectedPoseName, userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);
	tthis->userGenerator.GetPoseDetectionCap().StopPoseDetection(userId);
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
	tthis->broadcastUserChangeState(userId, USER_STATE_POSE_DETECTED);
}

}
