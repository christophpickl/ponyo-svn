#pragma once
#ifndef USERMANAGER_HPP_
#define USERMANAGER_HPP_

#include <ponyo/pnopenni/common_openni.hpp>
#include <ponyo/pnopenni/UserManagerException.hpp>

namespace pn {
class UserManager {
public:
	UserManager();
	virtual ~UserManager();

	void init(xn::Context&) throw(UserManagerException, OpenNiException);

	void start() throw(OpenNiException);
	bool isRunning();
	void stop() throw(OpenNiException);

private:
	static Log* LOG;
	xn::UserGenerator userGenerator;
	bool isPoseRequired;
	XnChar requiredPoseName[20];

	XnCallbackHandle callbackUser;
	XnCallbackHandle callbackCalibration;
	XnCallbackHandle callbackPose;

	static void XN_CALLBACK_TYPE onUserNew(xn::UserGenerator&, XnUserID, void* tthis);
	static void XN_CALLBACK_TYPE onUserLost(xn::UserGenerator&, XnUserID, void* tthis);
	static void XN_CALLBACK_TYPE onPoseDetected(xn::PoseDetectionCapability&, const XnChar*, XnUserID, void* tthis);
	static void XN_CALLBACK_TYPE onCalibrationStart(xn::SkeletonCapability&, XnUserID, void* tthis);
	static void XN_CALLBACK_TYPE onCalibrationEnd(xn::SkeletonCapability&, XnUserID, XnBool successfullyCalibrated, void* tthis);
};
}

#endif // USERMANAGER_HPP_
