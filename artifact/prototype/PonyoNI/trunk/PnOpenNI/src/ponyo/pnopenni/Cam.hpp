#pragma once
#ifndef CAM_HPP_
#define CAM_HPP_

#include <iostream>
#include <ponyo/pnopenni/common_openni.hpp>
#include <ponyo/pnopenni/CamInitDescriptor.hpp>

namespace pn {
class Cam {
public:
	Cam(xn::ImageGenerator&, std::string, CamInitDescriptor*); //const unsigned short, const unsigned short, const unsigned char, const unsigned char);
	virtual ~Cam();

	// deviceInfo.GetCreationInfo result example: "045e/02ae@36/62" (or: "045e/02ae@38/13")
	//   vendorId: 045e=Microsoft Corporation
	//   productId: 0x02ae=Xbox NUI Camera;   0x02b0=Xbox NUI Motor;   0x02ad=Xbox NUI Audio
	//   bus: 36
	//   address: 62
	const xn::ImageGenerator& getImageGenerator() const;
	const xn::ImageMetaData* getRecentImageData() const;

//	unsigned short getVendorId() const;
//	unsigned short getProductId() const;
//	unsigned char getBus() const;
//	unsigned char getAddress() const;
	std::string getCleanId() const;

	std::string toString();
	friend std::ostream& operator<<(std::ostream& os, const Cam& stringee) { // TODO operator does not work properly :-|
//		return os << "Cam[address=" << stringee.address << "]";
		os << "Cam[";
		os << "address=";
		os << stringee.cleanId;
//		os << reinterpret_cast<const char*>(stringee.address);
		os << "]";
		return os;
	}
private:
	static Log* LOG;

	xn::ImageGenerator imageGenerator;
	xn::ImageMetaData recentImageData;
	std::string cleanId;
	CamInitDescriptor* initDescriptor;

	XnCallbackHandle newDataAvailableCallbackHandle;

//	unsigned short vendorId;
//	unsigned short productId;
//	unsigned char bus;
//	unsigned char address;

	static void XN_CALLBACK_TYPE onImageDataAvailable(xn::ProductionNode&, void*);

};
}
#endif // CAM_HPP_
