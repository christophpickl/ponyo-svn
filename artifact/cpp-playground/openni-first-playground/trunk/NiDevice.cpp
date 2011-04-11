#include "ponyo_common.h"
#include "NiDevice.h"

NiDevice::NiDevice(xn::NodeInfo& pDeviceInfo, xn::NodeInfo& pImageInfo, xn::ImageGenerator& pImageGenerator, ImageSaver* pImageSaver)
	: deviceInfo(pDeviceInfo),
	  imageInfo(pImageInfo),
	  imageGenerator(pImageGenerator),
	  imageSaver(pImageSaver) {

	printf("new NiDevice(deviceInfo, imageInfo)\n");
	sscanf(this->deviceInfo.GetCreationInfo(), "%hx/%hx@%hhu/%hhu", &this->vendorId, &this->productId, &this->bus, &this->address);

//	std::stringstream ss;
//	ss << this->bus;
//	ss << "_";
//	ss << this->address;
//	ss >> this->busAndAddress;
	std::string s;
	std::ostringstream outStream;
	outStream << this->bus;
	outStream << "_";
	outStream << this->address;
	this->busAndAddress = outStream.str();


	XnCallbackHandle TODO_What_todo_with_this_imageCallbackHandle;
	XnStatus returnCode = this->imageGenerator.RegisterToNewDataAvailable(&NiDevice::onImageDataAvailable, this, TODO_What_todo_with_this_imageCallbackHandle);
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Could not register to new data available event for _some_ device!", returnCode); }
}

NiDevice::~NiDevice() {
	printf("delete ~NiDevice()\n");
}

void NiDevice::init() {
	printf("NiDevice.init()\n");
	XnStatus returnCode;

	// call virtual function to find available modes specifically for each device type
//	this->getAvailableModes();
	// set Depth resolution here only once... since no other mode for kinect is available -> deactivating setDepthResolution method!
//	setDepthOutputMode (getDefaultDepthMode ());
//	setImageOutputMode (getDefaultImageMode ());

	XnMapOutputMode imageOutputMode;
	imageOutputMode.nXRes = 640;
	imageOutputMode.nYRes = 480;
	imageOutputMode.nFPS = 30;
	returnCode = this->imageGenerator.SetMapOutputMode(imageOutputMode);
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Could not set output mode for _some_ device!", returnCode); }

}
void NiDevice::start() {
	printf("NiDevice.start()\n");

	if(this->imageGenerator.IsGenerating()) {
		printf("WARNING: image generator already started!\n");
		return;
	}
	XnStatus returnCode = this->imageGenerator.StartGenerating();
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Could not start image generator for _some_ device!", returnCode); }
}

/*static*/ void NiDevice::onImageDataAvailable(xn::ProductionNode& node, void* cookie) {
	printf("NiDevice.onImageDataAvailable() ... \n");
	NiDevice* tthis = reinterpret_cast<NiDevice*>(cookie);

    tthis->imageGenerator.WaitAndUpdateData();
    xn::ImageMetaData imageData;
    tthis->imageGenerator.GetMetaData(imageData);

    tthis->imageSaver->saveToDefault(imageData, tthis->busAndAddress);
}

void NiDevice::close() {
	printf("NiDevice.close()\n");

	if(this->imageGenerator.IsGenerating()) {
		XnStatus returnCode = this->imageGenerator.StopGenerating();
		if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Could not stop image generator for _some_ device!", returnCode); }
	} else {
		printf("WARNING: image generator not yet started!\n");
	}
}

void NiDevice::printToString() {
	printf("NiDevice: %s\n", this->deviceInfo.GetCreationInfo());
}

unsigned short NiDevice::getVendorId() {
	return this->vendorId;
}
unsigned short NiDevice::getProductId() {
	return this->productId;
}
unsigned char NiDevice::getBus() {
	return this->bus;
}
unsigned char NiDevice::getAddress() {
	return this->address;
}
