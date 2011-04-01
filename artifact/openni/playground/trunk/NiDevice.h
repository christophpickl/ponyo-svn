#ifndef NIDEVICE_H_
#define NIDEVICE_H_

#include <XnCppWrapper.h>
#include "ImageSaver.h"

class NiDevice {
public:
	NiDevice(xn::NodeInfo&, xn::NodeInfo&, xn::ImageGenerator&, ImageSaver*);
	virtual ~NiDevice();

	void init();
	void start();
	void close();

	void printToString();

private:
	xn::NodeInfo deviceInfo;
	xn::NodeInfo imageInfo;
	xn::ImageGenerator imageGenerator;
	ImageSaver* imageSaver; // TODO remove afterwards

	static void XN_CALLBACK_TYPE onImageDataAvailable(xn::ProductionNode&, void*);
};

#endif /* NIDEVICE_H_ */
