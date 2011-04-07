#pragma once
#ifndef IMAGECONVERTER_HPP_
#define IMAGECONVERTER_HPP_

#include "common_openni.hpp"
#include "pninclude_opencv.h"

namespace pn {
class ImageConverter {
public:
	ImageConverter();
	virtual ~ImageConverter();

	void convertMeta2Mat(cv::Mat& targetMat, const xn::ImageMetaData* sourceMeta);
private:
	static Log* LOG;
};
}

#endif // IMAGECONVERTER_HPP_
