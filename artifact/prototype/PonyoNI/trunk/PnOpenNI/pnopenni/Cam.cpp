#include <sstream>

#include "Cam.hpp"

namespace pn {

Log* Cam::LOG = NEW_LOG(__FILE__);

Cam::Cam(xn::ImageGenerator& pImageGenerator, std::string pCleanId, CamInitDescriptor* pInitDescriptor) : // const unsigned short pVendorId, const unsigned short pProductId, const unsigned char pBus, const unsigned char pAddress) :
	imageGenerator(pImageGenerator),
	cleanId(pCleanId),
	initDescriptor(pInitDescriptor)
//	vendorId(pVendorId),
//	productId(pProductId),
//	bus(pBus),
//	address(pAddress)
	{
	LOG->debug("new Cam(..)");

	if(this->initDescriptor->isImageGeneratorRequired()) {
		printf("register A\n");
		CHECK_RC(this->imageGenerator.RegisterToNewDataAvailable(&Cam::onImageDataAvailable, this, this->newDataAvailableCallbackHandle),
			"imageGenerator.RegisterToNewDataAvailable(..)");
		printf("register B\n");
	}
}

Cam::~Cam() {
	LOG->debug("~Cam() ... unregistering callback");

	this->imageGenerator.UnregisterFromNewDataAvailable(this->newDataAvailableCallbackHandle);
}
std::string Cam::getCleanId() const {
	return this->cleanId;
}

/*static*/ void Cam::onImageDataAvailable(xn::ProductionNode& node, void* cookie) {
	Cam* tthis = reinterpret_cast<Cam*>(cookie);
	printf(".\n");
//	printf("onImageDataAvailable(..) >> tthis->imageGenerator.WaitAndUpdateData();\n");
    tthis->imageGenerator.WaitAndUpdateData();
    tthis->imageGenerator.GetMetaData(tthis->recentImageData);

    // TODO lock; write MD; unlock
}

const xn::ImageGenerator& Cam::getImageGenerator() const {
//	xn::ImageGenerator& tmp = this->imageGenerator;
	return this->imageGenerator;
}

const xn::ImageMetaData* Cam::getRecentImageData() const {
	return &this->recentImageData;
}

//unsigned short Cam::getVendorId() const {
//	return this->vendorId;
//}
//unsigned short Cam::getProductId() const {
//	return this->productId;
//}
//unsigned char Cam::getBus() const {
//	return this->bus;
//}
//unsigned char Cam::getAddress() const {
//	return this->address;
//}

std::string Cam::toString() {
	std::stringstream ss;
	ss << *this;
	return ss.str();
}

}
