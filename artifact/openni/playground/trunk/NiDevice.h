#ifndef NIDEVICE_H_
#define NIDEVICE_H_

#include <XnCppWrapper.h>

class NiDevice {
public:
	NiDevice(xn::NodeInfo&, xn::NodeInfo&, xn::ImageGenerator&);
	virtual ~NiDevice();

	void start();
	void stop();

	void printToString();

private:
	xn::NodeInfo deviceInfo;
	xn::NodeInfo imageInfo;
	xn::ImageGenerator imageGenerator;
};

#endif /* NIDEVICE_H_ */
