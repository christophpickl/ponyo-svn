#include "ponyo_common.h"
#include "NiDevice.h"

NiDevice::NiDevice(xn::NodeInfo& pDeviceInfo, xn::NodeInfo& pImageInfo, xn::ImageGenerator& pImageGenerator)
	: deviceInfo(pDeviceInfo),
	  imageInfo(pImageInfo),
	  imageGenerator(pImageGenerator) {
	printf("new NiDevice(deviceInfo, imageInfo)\n");
}

NiDevice::~NiDevice() {
	printf("delete ~NiDevice()\n");
}

void NiDevice::start() {
	printf("NiDevice.start()");

}

void NiDevice::stop() {
	printf("NiDevice.stop()");

}

void NiDevice::printToString() {
	printf("NiDevice: %s\n", this->deviceInfo.GetCreationInfo());
	/*
	 * eg: 045e/02ae@36/62
	 *     045e/02ae@38/13
	 *
	 * unsigned short vendor_id;
	 * unsigned short product_id;
	 * unsigned char bus;
	 * unsigned char address;
	 * sscanf (deviceInfo.GetCreationInfo(), "%hx/%hx@%hhu/%hhu", &vendor_id, &product_id, &bus, &address);
	 */
}
