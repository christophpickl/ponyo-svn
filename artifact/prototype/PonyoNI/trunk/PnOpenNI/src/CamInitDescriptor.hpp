#pragma once
#ifndef CAMINITDESCRIPTOR_H_
#define CAMINITDESCRIPTOR_H_

#include "common_openni.hpp"

namespace pn {
class CamInitDescriptor {
public:
	CamInitDescriptor(bool, bool, XnMapOutputMode&);

	const bool isImageGeneratorRequired() const;
	const bool isUserGeneratorRequired() const;
	const XnMapOutputMode& getImageOutputMode() const;

private:
	bool imageGeneratorRequired;
	bool userGeneratorRequired;
	XnMapOutputMode imageOutputMode;

};
}
#endif // CAMINITDESCRIPTOR_H_
