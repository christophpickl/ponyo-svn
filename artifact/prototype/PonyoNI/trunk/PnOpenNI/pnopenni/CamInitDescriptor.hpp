#pragma once
#ifndef CAMINITDESCRIPTOR_H_
#define CAMINITDESCRIPTOR_H_

#include <ponyo/pnopenni/common_openni.hpp>

namespace pn {
class CamInitDescriptor {
public:
	CamInitDescriptor(bool, bool, bool, XnMapOutputMode&);

	const bool isImageGeneratorRequired() const;
	const bool isDepthGeneratorRequired() const;
	const bool isUserGeneratorRequired() const;
	const XnMapOutputMode getMapOutputMode() const;

private:
	bool imageGeneratorRequired;
	bool depthGeneratorRequired;
	bool userGeneratorRequired;
	XnMapOutputMode mapOutputMode;

};
}
#endif // CAMINITDESCRIPTOR_H_
