#ifndef NIENHANCED_H_
#define NIENHANCED_H_

#include <string>
#include <XnCppWrapper.h>
#include "ImageSaver.h"
//#include <XnOpenNI.h>
//#include <XnCodecIDs.h>

class NiEnhanced {
public:
	NiEnhanced();
	virtual ~NiEnhanced();

	void initFromXml(std::string);
	void waitForUpdate();
	void close();

private:
	xn::Context context;
	xn::DepthGenerator depthGenerator;
	xn::ImageGenerator imageGenerator;
	xn::UserGenerator userGenerator;

	xn::ImageMetaData imageMetaData;
	XnChar requiredPoseName[20];

	ImageSaver* imageSaver; // TODO remove afterwards

	static void XN_CALLBACK_TYPE onNewUser(xn::UserGenerator&, XnUserID, void*);
	static void XN_CALLBACK_TYPE onUserLost(xn::UserGenerator&, XnUserID, void*);
	static void XN_CALLBACK_TYPE onPoseDetected(xn::PoseDetectionCapability&, const XnChar*, XnUserID, void*);
	static void XN_CALLBACK_TYPE onCalibrationStarted(xn::SkeletonCapability&, XnUserID, void*);
	static void XN_CALLBACK_TYPE onCalibrationEnded(xn::SkeletonCapability&, XnUserID, XnBool, void*);

};

#endif /* NIENHANCED_H_ */
