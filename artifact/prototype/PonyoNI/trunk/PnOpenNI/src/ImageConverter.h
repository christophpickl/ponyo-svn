#pragma once
#ifndef IMAGECONVERTER_H_
#define IMAGECONVERTER_H_

#include "common_openni.hpp"

namespace pn {
class ImageConverter {
public:
	ImageConverter();
	virtual ~ImageConverter();
private:
	static Log* LOG;
};
}

#endif // IMAGECONVERTER_H_
