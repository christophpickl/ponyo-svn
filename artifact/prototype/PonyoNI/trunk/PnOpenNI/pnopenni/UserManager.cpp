#include "UserManager.hpp"

namespace pn {

Log* UserManager::LOG = NEW_LOG(__FILE__)

UserManager::UserManager() {
	LOG->debug("new UserManager()");
}
UserManager::~UserManager() {
	LOG->debug("~UserManager() ... unregistering callbacks");

	this->userGenerator.UnregisterUserCallbacks(this->callbackUser);
	this->userGenerator.GetSkeletonCap().UnregisterCalibrationCallbacks(this->callbackCalibration);
	this->userGenerator.GetPoseDetectionCap().UnregisterFromPoseCallbacks(this->callbackPose);
}

void UserManager::init(xn::Context& context) throw(UserManagerException, OpenNiException) {
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

	// -------------------------------------------------------
	LOG->debug("Registering callbacks.");

	xn::SkeletonCapability skeletonCap = this->userGenerator.GetSkeletonCap();
	CHECK_RC(userGenerator.RegisterUserCallbacks(&UserManager::onUserNew, &UserManager::onUserLost, this, this->callbackUser),
		"Registering user callbacks for user generator failed!");

	CHECK_RC(skeletonCap.RegisterCalibrationCallbacks(&UserManager::onCalibrationStart, &UserManager::onCalibrationEnd, this, this->callbackCalibration),
		"Registering calibration callbacks for user generator failed!");

	if(skeletonCap.NeedPoseForCalibration()) {
		this->isPoseRequired = true;
		if(!this->userGenerator.IsCapabilitySupported(XN_CAPABILITY_POSE_DETECTION)) {
			throw UserManagerException("A pose is required but not supported by your hardware!", AT);
		}

		this->userGenerator.GetPoseDetectionCap().RegisterToPoseCallbacks(&UserManager::onPoseDetected, NULL/*onPoseDetectedEnd*/, this, this->callbackPose);
		skeletonCap.GetCalibrationPose(this->requiredPoseName);

//		printf("Required pose name: '%s'\n", this->requiredPoseName);
		std::cout << "Calibration pose name: '" << this->requiredPoseName << "'" << std::endl;

	} else {
		this->isPoseRequired = false;
	}
	this->userGenerator.GetSkeletonCap().SetSkeletonProfile(XN_SKEL_PROFILE_ALL); //  we want to have all joints

	//this->userGenerator.GetSkeletonCap().SetSmoothing(0.8);
	//xnSetMirror(depth, xxMirrorMode);

	// iterating over USER production nodes ===> returns only ONE instance ... interesting.
	// **********************************************************************************************************
	//	xn::NodeInfoList userInfoList;
	//	LOG->trace(">> context.EnumerateProductionTrees(XN_NODE_TYPE_USER, ..)");
	//	CHECK_RC(context.EnumerateProductionTrees(XN_NODE_TYPE_USER, NULL, userInfoList, NULL), "context.EnumerateProductionTrees(USER)");
	//
	//	std::vector<xn::NodeInfo> imageInfos;
	//	for (xn::NodeInfoList::Iterator it = userInfoList.Begin(); it != userInfoList.End(); ++it) {
	//		xn::NodeInfo info = *it;
	//		printf("NodeInfo UserNode: creationInfo=%s; descriptionName=%s\n", info.GetCreationInfo(), info.GetDescription().strName);
	//		imageInfos.push_back(*it);
	//	}

	LOG->debug("init(context) END");
}

void UserManager::start() throw(OpenNiException) {
	if(this->userGenerator.IsGenerating()) {
		LOG->warn("start() ... aborting, as already generating!");
		return;
	}

	LOG->info("start()");
	CHECK_RC(this->userGenerator.StartGenerating(), "Starting user generator failed!");
}

void UserManager::stop() throw(OpenNiException) {
	if(!this->userGenerator.IsGenerating()) {
		LOG->warn("stop() ... aborting, as not generating!");
		return;
	}

	LOG->info("stop()");
	CHECK_RC(this->userGenerator.StopGenerating(), "Stopping user generator failed!");
}

/*static*/ void XN_CALLBACK_TYPE UserManager::onUserNew(xn::UserGenerator& generator, XnUserID userId, void* cookie) {
	printf("UserManager.onNewUser(generator, userId=%d, cookie)\n", userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);

	printf("Requesting calibration for new user.");
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
}

/*static*/ void XN_CALLBACK_TYPE UserManager::onUserLost(xn::UserGenerator& generator, XnUserID userId, void* cookie) {
	printf("UserManager.onUserLost(generator, userId=%d, cookie)\n", userId);
}

/*static*/ void XN_CALLBACK_TYPE UserManager::onPoseDetected(xn::PoseDetectionCapability& capability, const XnChar* detectedPoseName, XnUserID userId, void* cookie) {
	printf("UserManager.Pose %s detected for user %d\n", detectedPoseName, userId);

	UserManager* tthis = static_cast<UserManager*>(cookie);
	tthis->userGenerator.GetPoseDetectionCap().StopPoseDetection(userId);
	tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
}

/*static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationStart(xn::SkeletonCapability& capability, XnUserID userId, void* cookie) {
	printf("Calibration started for user %d\n", userId);
}

/*static*/ void XN_CALLBACK_TYPE UserManager::onCalibrationEnd(xn::SkeletonCapability& capability, XnUserID userId, XnBool successfullyCalibrated, void* cookie) {
	UserManager* tthis = static_cast<UserManager*>(cookie);

	if (successfullyCalibrated) {
		printf("Calibration complete, start tracking user %d\n", userId);
		tthis->userGenerator.GetSkeletonCap().StartTracking(userId);
	} else {
		printf("Calibration failed for user %d\n", userId);
		tthis->userGenerator.GetSkeletonCap().RequestCalibration(userId, TRUE);
	}
}

}
