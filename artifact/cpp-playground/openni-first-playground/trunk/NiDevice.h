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

	// deviceInfo.GetCreationInfo result example: "045e/02ae@36/62" (or: "045e/02ae@38/13")
	//   vendorId: 045e=Microsoft Corporation
	//   productId: 0x02ae=Xbox NUI Camera;   0x02b0=Xbox NUI Motor;   0x02ad=Xbox NUI Audio
	//   bus: 36
	//   address: 62
	unsigned short getVendorId();
	unsigned short getProductId();
	unsigned char getBus();
	unsigned char getAddress();

	void printToString();

private:
	unsigned short vendorId;
	unsigned short productId;
	unsigned char bus;
	unsigned char address;
	std::string busAndAddress; // is a unique combination; used for image filename

	xn::NodeInfo deviceInfo;
	xn::NodeInfo imageInfo;
	xn::ImageGenerator imageGenerator;
	ImageSaver* imageSaver; // TODO remove afterwards

	static void XN_CALLBACK_TYPE onImageDataAvailable(xn::ProductionNode&, void*);
};

#endif /* NIDEVICE_H_ */
