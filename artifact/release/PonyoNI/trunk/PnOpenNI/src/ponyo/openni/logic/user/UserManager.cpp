#include <ponyo/openni/OpenNIUtils.hpp>
#include <ponyo/openni/logic/user/UserManager.hpp>

namespace pn {

Log* UserManager::LOG = NEW_LOG();

UserManager::UserManager(UserStateCallback pUserCallback, JointPositionCallback pJointCallback) :
		userCallback(pUserCallback),
		jointCallback(pJointCallback),
		skeletonCapability(NULL), callbacksRegistered(false) {
	LOG->debug("new UserManager(..)");
	// TODO write NULL CHECK macro
	if(pUserCallback == NULL) { throw NullArgumentException("UserStateCallback", AT); }
	if(pJointCallback == NULL) { throw NullArgumentException("JointPositionCallback", AT); }
}

UserManager::~UserManager() {
	LOG->debug("~UserManager()");
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
	OpenNIUtils::dumpJointAvailability(this->skeletonCapability);


	this->registerCallbacks();
	this->skeletonCapability.SetSkeletonProfile(XN_SKEL_PROFILE_ALL); //  we want to have all joints
	// TODO CHECK_XN(this->skeletonCapability.SetSmoothing(0.0f), "Setting smoothing for skeleton capability failed!");

	this->userGenerator.StartGenerating();

	LOG->debug("init(context) END");
}

/*private*/ inline void UserManager::registerCallbacks() throw(OpenNiException) {
	LOG->debug("registerCallbacks()");

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

	this->callbacksRegistered = true;
}

/*public*/ void UserManager::unregister() {
	LOG->debug("unregister()");

	if(this->callbacksRegistered == false) {
		return;
	}

	this->userGenerator.UnregisterUserCallbacks(this->callbackUser);
	this->userGenerator.GetSkeletonCap().UnregisterCalibrationCallbacks(this->callbackCalibration);
	this->userGenerator.GetPoseDetectionCap().UnregisterFromPoseCallbacks(this->callbackPose);
	this->callbacksRegistered = false;
}

/*public*/ void UserManager::update() throw(OpenNiException) {
	const XnUInt16 constUserCount = this->userGenerator.GetNumberOfUsers();
	XnUserID userIds[constUserCount];
	XnUInt16 userCount = constUserCount;

	// print some Generator infos:
	// - isDataNew is false on very first invocation!
	// - timestamp is a veeery long timestamp ;)
	// - frameID is a sequence starting by 1
//	printf("\nGenerator: IsDataNew=%i; timestamp=%llu, frameID=%i\n\n", this->userGenerator.IsDataNew(), this->userGenerator.GetTimestamp(), this->userGenerator.GetFrameID());

	CHECK_XN(this->userGenerator.GetUsers(userIds, userCount), "Getting users failed!");

	for (int i = 0; i < userCount; ++i) {
		XnUserID userId = userIds[i];
		if (!this->skeletonCapability.IsTracking(userId)) {
			// user is detected (new), but not yet calibrated; skip. // still, could have checked with IsTracking(), but because of performance reasons...
			continue;
		}
		// TODO use CoM
//		XnPoint3D com;
//		CHECK_XN(this->userGenerator.GetCoM(userId, com), "Getting CoM failed!");
//		printf("broadcastCenterOfMass coordinates: %f x %f x %f\n", com.X, com.Y, com.Z);
//		somehow configure what client needs, in a 100% performant way without always checking != NULL ... this->centerOfMassCallback(userId, XnVector3D);

		this->broadcastJointPositions(userId, XN_SKEL_HEAD, 0); // FIXME create own typedef for joint positions
		this->broadcastJointPositions(userId, XN_SKEL_NECK, 1);
		this->broadcastJointPositions(userId, XN_SKEL_TORSO, 2);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_WAIST, 3);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_COLLAR, 4);
		this->broadcastJointPositions(userId, XN_SKEL_LEFT_SHOULDER, 5);
		this->broadcastJointPositions(userId, XN_SKEL_LEFT_ELBOW, 6);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_WRIST, 7);
		this->broadcastJointPositions(userId, XN_SKEL_LEFT_HAND, 8);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_FINGERTIP, 9);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_COLLAR, 10);
		this->broadcastJointPositions(userId, XN_SKEL_RIGHT_SHOULDER, 11);
		this->broadcastJointPositions(userId, XN_SKEL_RIGHT_ELBOW, 12);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_WRIST, 13);
		this->broadcastJointPositions(userId, XN_SKEL_RIGHT_HAND, 14);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_FINGERTIP, 15);
		this->broadcastJointPositions(userId, XN_SKEL_LEFT_HIP, 16);
		this->broadcastJointPositions(userId, XN_SKEL_LEFT_KNEE, 17);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_LEFT_ANKLE, 18);
		this->broadcastJointPositions(userId, XN_SKEL_LEFT_FOOT, 19);
		this->broadcastJointPositions(userId, XN_SKEL_RIGHT_HIP, 20);
		this->broadcastJointPositions(userId, XN_SKEL_RIGHT_KNEE, 21);
//		this->broadcastJointPositions(currentUserId, XN_SKEL_RIGHT_ANKLE, 22);
		this->broadcastJointPositions(userId, XN_SKEL_RIGHT_FOOT, 23);
	}
}

/*private*/ inline void UserManager::broadcastJointPositions(
		UserId userId,
		XnSkeletonJoint jointEnum,
		int jointId
	) {

	// TODO add optional joint orientation support
//	XnSkeletonJointTransformation jointTransformation;
//	CHECK_XN(this->skeletonCapability.GetSkeletonJoint(userId, jointEnum, jointTransformation), "Get joint transformation failed!");
//	XnSkeletonJointOrientation jointOrientation = jointTransformation.orientation;

	XnSkeletonJointPosition jointPosition;
	CHECK_XN(this->skeletonCapability.GetSkeletonJointPosition(userId, jointEnum, jointPosition), "Get joint position failed!");

	if(jointPosition.fConfidence > PN_CONFIDENCE_LIMIT) {
		this->jointCallback(userId, jointId,
			jointPosition.position.X, jointPosition.position.Y, jointPosition.position.Z);
	}
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onUserNew(
		xn::UserGenerator& generator,
		XnUserID userId,
		void* cookie
	) {
	LOG->debug2("onUserNew(generator, userId=%i, cookie)", userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);

	tthis->broadcastUserChangeState(userId, USER_STATE_NEW);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationStarted(
		xn::SkeletonCapability& skeleton,
		XnUserID userId,
		void* cookie
	) {
	LOG->debug2("onCalibrationStarted(skeleton, userId=%i, cookie)", userId);
	UserManager* tthis = static_cast<UserManager*>(cookie);

	tthis->broadcastUserChangeState(userId, USER_STATE_CALIBRATION_STARTED);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationEnded(
		xn::SkeletonCapability& skeleton,
		XnUserID userId,
		XnBool successfullyCalibrated,
		void* cookie
	) {
	LOG->debug2("onCalibrationEnded(skeleton, userId=%i, successfullyCalibrated=%i, cookie)", userId, successfullyCalibrated);
	UserManager* tthis = static_cast<UserManager*>(cookie);

	UserState state;
	if (successfullyCalibrated) {
		printf("Calibration complete, start tracking user with ID %i.\n", userId);
		tthis->userGenerator.GetSkeletonCap().StartTracking(userId);
		state = USER_STATE_CALIBRATION_ENDED_SUCCESFULLY;

	} else {
		printf("Calibration failed for user with ID %i, retrying...\n", userId);
		tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
		state = USER_STATE_CALIBRATION_ENDED_UNSUCCESFULLY;
	}

	tthis->broadcastUserChangeState(userId, state);
}

// FIXME when gets this invoked?!?
/*private static*/ void XN_CALLBACK_TYPE UserManager::onPoseDetected(
		xn::PoseDetectionCapability& skeleton,
		const XnChar* detectedPoseName,
		XnUserID userId,
		void* cookie
	) {
	LOG->debug2("onPoseDetected(skeleton, detectedPoseName=%s, userId=%i, cookie)", detectedPoseName, userId);
	UserManager* tthis = static_cast<UserManager*>(cookie);

	tthis->userGenerator.GetPoseDetectionCap().StopPoseDetection(userId);
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);

	tthis->broadcastUserChangeState(userId, USER_STATE_POSE_DETECTED);
}

/*private static*/ void XN_CALLBACK_TYPE UserManager::onUserLost(
		xn::UserGenerator& generator,
		XnUserID userId,
		void* cookie
	) {
	LOG->debug2("onUserLost(generator, userId=%i, cookie)", userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);
	tthis->broadcastUserChangeState(userId, USER_STATE_LOST);
}

/*private*/ inline void UserManager::broadcastUserChangeState(
		UserId userId,
		UserState userState
	) {
	this->userCallback(userId, userState);
}

}
