#include "CamInitDescriptor.hpp"

namespace pn {

CamInitDescriptor::CamInitDescriptor(
		bool pImageGeneratorRequired,
		bool pDepthGeneratorRequired,
		bool pUserGeneratorRequired,
		XnMapOutputMode& pMapOutputMode
		) :
		imageGeneratorRequired(pImageGeneratorRequired),
		depthGeneratorRequired(pDepthGeneratorRequired),
		userGeneratorRequired(pUserGeneratorRequired),
		mapOutputMode(pMapOutputMode)
	{
	// nothing to do, everything done in init list
}

const bool CamInitDescriptor::isImageGeneratorRequired() const {
	return this->imageGeneratorRequired;
}

const bool CamInitDescriptor::isDepthGeneratorRequired() const {
	return this->depthGeneratorRequired;
}

const bool CamInitDescriptor::isUserGeneratorRequired() const {
	return this->userGeneratorRequired;
}

const XnMapOutputMode CamInitDescriptor::getMapOutputMode() const {
	return this->mapOutputMode;
}

}
