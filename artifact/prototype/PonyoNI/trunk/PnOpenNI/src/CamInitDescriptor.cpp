#include "CamInitDescriptor.hpp"

namespace pn {

CamInitDescriptor::CamInitDescriptor(
		bool pImageGeneratorRequired,
		bool pUserGeneratorRequired,
		XnMapOutputMode& pImageOutputMode
		) :
		imageGeneratorRequired(pImageGeneratorRequired),
		userGeneratorRequired(pUserGeneratorRequired),
		imageOutputMode(pImageOutputMode)
	{
	// nothing to do, everything done in init list
}

const bool CamInitDescriptor::isImageGeneratorRequired() const {
	return this->imageGeneratorRequired;
}

const bool CamInitDescriptor::isUserGeneratorRequired() const {
	return this->userGeneratorRequired;
}

const XnMapOutputMode& CamInitDescriptor::getImageOutputMode() const {
	return this->imageOutputMode;
}

}
