#include <ponyo/openni/UserManager.hpp>

namespace pn {

Log* UserManager::LOG = NEW_LOG();

UserManager::UserManager(UserStateCallback pUserStateCallback, JointPositionCallback pJointPositionCallback) :
		skeletonCapability(NULL),
		userStateCallback(pUserStateCallback),
		jointPositionCallback(pJointPositionCallback)
{
	LOG->debug("new UserManager(userStateCallback, jointPositionCallback)");

	if(this->userStateCallback == NULL) {
		throw NullArgumentException("null", AT);
	}
}

UserManager::~UserManager() {
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

	LOG->debug("Setting up user generator node.");

	const XnStatus foundExistingUserGenerator = context.FindExistingNode(XN_NODE_TYPE_USER, this->userGenerator);
	if(foundExistingUserGenerator != XN_STATUS_OK) {
		LOG->debug("Not found existing UserGenerator; going to create one.");
		CHECK_XN(this->userGenerator.Create(context), "userGenerator.Create(context)");
	}

	if(!this->userGenerator.IsCapabilitySupported(XN_CAPABILITY_SKELETON)) {
		throw UserManagerException("Supplied user generator doesn't support skeleton", AT);
	}
	this->skeletonCapability = this->userGenerator.GetSkeletonCap();

	// -------------------------------------------------------
	LOG->debug("Registering callbacks.");

	CHECK_XN(this->userGenerator.RegisterUserCallbacks(
		&UserManager::onUserNew, &UserManager::onUserLost, this, this->callbackUser),
			"Registering user callbacks for user generator failed!");

	CHECK_XN(this->skeletonCapability.RegisterCalibrationCallbacks(
		&UserManager::onCalibrationStarted, &UserManager::onCalibrationEnded, this, this->callbackCalibration),
			"Registering calibration callbacks for user generator failed!");

	this->poseRequired = this->skeletonCapability.NeedPoseForCalibration() == TRUE;
	if(this->poseRequired) {
		if(!this->userGenerator.IsCapabilitySupported(XN_CAPABILITY_POSE_DETECTION)) {
			throw UserManagerException("A pose is required but not supported by your hardware!", AT);
		}

		this->userGenerator.GetPoseDetectionCap().RegisterToPoseCallbacks(&UserManager::onPoseDetected, NULL/*onPoseDetectedEnd*/, this, this->callbackPose);
		this->skeletonCapability.GetCalibrationPose(this->requiredPoseName);

		std::cout << "Calibration pose name: '" << this->requiredPoseName << "'" << std::endl;
	} else {
		LOG->debug("No calibration pose required.");
	}

	this->skeletonCapability.SetSkeletonProfile(XN_SKEL_PROFILE_ALL); //  we want to have all joints
	// TODO CHECK_XN(this->skeletonCapability.SetSmoothing(0.0f), "Setting smoothing for skeleton capability failed!");

	this->userGenerator.StartGenerating();

	LOG->debug("init(context) END");
}


/*public*/ void UserManager::update() throw(OpenNiException) {
	const XnUInt16 constUserCount = this->userGenerator.GetNumberOfUsers();
	XnUserID userIds[constUserCount];
	XnUInt16 userCount = constUserCount;
	CHECK_XN(this->userGenerator.GetUsers(userIds, userCount), "Getting users failed!");

	for (int i = 0; i < userCount; ++i) {
		XnUserID currentUserId = userIds[i];
		if (!this->skeletonCapability.IsTracking(currentUserId)) {
			// user is detected (new), but not yet calibrated; skip. // still, could have checked with IsTracking(), but because of performance reasons...
			continue;
		}

		this->broadcastJointPositions(currentUserId, XN_SKEL_HEAD, 0); // FIXME create own typedef for joint positions
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
	}
}

/*private*/ inline void UserManager::broadcastJointPositions(
		UserId userId,
		XnSkeletonJoint jointEnum,
		int jointId
	) {
	// TODO add optional joint orientation support

	XnSkeletonJointPosition jointPosition;
	CHECK_XN(this->skeletonCapability.GetSkeletonJointPosition(userId, jointEnum, jointPosition), "Get joint position failed");

	if(jointPosition.fConfidence < 0.5) {
		return; // skip bad data
	}
//	printf("joint position: %i.X = %f\n", jointEnum, jointPosition.position.X);
	this->jointPositionCallback(userId, jointId, jointPosition.position.X, jointPosition.position.Y, jointPosition.position.Z);
}

/*private*/ inline void UserManager::broadcastUserChangeState(
		UserId userId,
		UserState userState
	) {
	this->userStateCallback(userId, userState);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onUserNew(
		xn::UserGenerator& generator,
		XnUserID userId,
		void* cookie
	) {
	LOG->debug("onUserNew(generator, userId, cookie)");

	UserManager* tthis = static_cast<UserManager*>(cookie);
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);

	tthis->broadcastUserChangeState(userId, USER_STATE_NEW);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onUserLost(
		xn::UserGenerator& generator,
		XnUserID userId,
		void* cookie
	) {
	LOG->debug("onUserLost(generator, userId, cookie)");

	UserManager* tthis = static_cast<UserManager*>(cookie);
	tthis->broadcastUserChangeState(userId, USER_STATE_LOST);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationStarted(
		xn::SkeletonCapability& capability,
		XnUserID userId,
		void* cookie
	) {
	LOG->debug("onCalibrationStarted(...)");
	UserManager* tthis = static_cast<UserManager*>(cookie);

	tthis->broadcastUserChangeState(userId, USER_STATE_CALIBRATION_STARTED);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationEnded(
		xn::SkeletonCapability& capability,
		XnUserID userId,
		XnBool successfullyCalibrated,
		void* cookie
	) {
	LOG->debug("onCalibrationEnded(...)\n");
	UserManager* tthis = static_cast<UserManager*>(cookie);

	UserState state;
	if (successfullyCalibrated) {
		LOG->trace("Calibration complete, start tracking user.\n");
		tthis->userGenerator.GetSkeletonCap().StartTracking(userId);
		state = USER_STATE_CALIBRATION_ENDED_SUCCESFULLY;

	} else {
		LOG->trace("Calibration failed.\n");
		tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
		state = USER_STATE_CALIBRATION_ENDED_UNSUCCESFULLY;
	}

	tthis->broadcastUserChangeState(userId, state);
}

// FIXME when is this invoked??
/*private static*/ void XN_CALLBACK_TYPE UserManager::onPoseDetected(
		xn::PoseDetectionCapability& capability,
		const XnChar* detectedPoseName,
		XnUserID userId,
		void* cookie
	) {
	LOG->debug("onPoseDetected(...)");
	UserManager* tthis = static_cast<UserManager*>(cookie);

	tthis->userGenerator.GetPoseDetectionCap().StopPoseDetection(userId);
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);

	tthis->broadcastUserChangeState(userId, USER_STATE_POSE_DETECTED);
}

}
