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

	void convertImageMetaData2cvMat(cv::Mat& targetMat, const xn::ImageMetaData* sourceData);
private:
	static Log* LOG;
};
}

#endif // IMAGECONVERTER_HPP_
