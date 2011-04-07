#include <sstream>

#include "Cam.hpp"

namespace pn {

Log* Cam::LOG = NEW_LOG(__FILE__);

Cam::Cam(xn::ImageGenerator& pImageGenerator, const unsigned short pVendorId, const unsigned short pProductId, const unsigned char pBus, const unsigned char pAddress) :
	imageGenerator(pImageGenerator),
	vendorId(pVendorId),
	productId(pProductId),
	bus(pBus),
	address(pAddress)
	{
	LOG->debug("new Cam(..)");

	XnCallbackHandle TODO_What_todo_with_this_imageCallbackHandle;
	CHECK_RC(this->imageGenerator.RegisterToNewDataAvailable(&Cam::onImageDataAvailable, this, TODO_What_todo_with_this_imageCallbackHandle), "imageGenerator.RegisterToNewDataAvailable(..)");
}

Cam::~Cam() {
	LOG->debug("~Cam()");
}

/*static*/ void Cam::onImageDataAvailable(xn::ProductionNode& node, void* cookie) {
	Cam* tthis = reinterpret_cast<Cam*>(cookie);

	printf("onImageDataAvailable(..) >> tthis->imageGenerator.WaitAndUpdateData();\n");
    tthis->imageGenerator.WaitAndUpdateData();
    xn::ImageMetaData imageData;
    tthis->imageGenerator.GetMetaData(imageData);

    // TODO
    // lock
    // write MD
    // unlock
}

const xn::ImageGenerator& Cam::getImageGenerator() const {
//	xn::ImageGenerator& tmp = this->imageGenerator;
	return this->imageGenerator;
}

unsigned short Cam::getVendorId() const {
	return this->vendorId;
}
unsigned short Cam::getProductId() const {
	return this->productId;
}
unsigned char Cam::getBus() const {
	return this->bus;
}
unsigned char Cam::getAddress() const {
	return this->address;
}

std::string Cam::toString() {
	std::stringstream ss;
	ss << *this;
	return ss.str();
}

}
