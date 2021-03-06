#pragma once
#ifndef USERMANAGER_HPP_
#define USERMANAGER_HPP_

#include <ponyo/openni/pnopenni_inc.hpp>
#include <ponyo/openni/logic/user/UserManagerException.hpp>
#include <ponyo/openni/logic/user/UserState.hpp>

namespace pn {

class UserManager {
public:
	UserManager(UserStateCallback, JointPositionCallback);
	virtual ~UserManager();

//	void registerUserStateCallback(UserStateCallback, void* cookie = NULL);
//	void registerJointPositionCallback(JointPositionCallback, void* cookie = NULL);

	void init(xn::Context&) throw(UserManagerException, OpenNiException);
	void update() throw(OpenNiException);
	void unregister();

private:
	static Log* LOG;
	xn::UserGenerator userGenerator;
	xn::SkeletonCapability skeletonCapability;

	UserStateCallback userCallback;
	JointPositionCallback jointCallback;

	bool poseRequired;
	XnChar requiredPoseName[20];

	XnCallbackHandle callbackUser;
	XnCallbackHandle callbackCalibration;
	XnCallbackHandle callbackPose;
	bool callbacksRegistered;

	void registerCallbacks() throw(OpenNiException);
	void broadcastUserChangeState(UserId userId, UserState userState);
	void broadcastJointPositions(UserId userId, XnSkeletonJoint jointEnum, int jointId);

	static void XN_CALLBACK_TYPE onUserNew(xn::UserGenerator&, XnUserID, void* tthis);
	static void XN_CALLBACK_TYPE onUserLost(xn::UserGenerator&, XnUserID, void* tthis);
	static void XN_CALLBACK_TYPE onPoseDetected(xn::PoseDetectionCapability&, const XnChar*, XnUserID, void* tthis);
	static void XN_CALLBACK_TYPE onCalibrationStarted(xn::SkeletonCapability&, XnUserID, void* tthis);
	static void XN_CALLBACK_TYPE onCalibrationEnded(xn::SkeletonCapability&, XnUserID, XnBool successfullyCalibrated, void* tthis);
};
}

#endif // USERMANAGER_HPP_
