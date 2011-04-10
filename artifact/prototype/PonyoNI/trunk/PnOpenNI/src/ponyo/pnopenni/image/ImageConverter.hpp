#pragma once
#ifndef IMAGECONVERTER_HPP_
#define IMAGECONVERTER_HPP_

#include <ponyo/pncommon/pninclude_opencv.h>
#include <ponyo/pnopenni/PnOpenNI.hpp>

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
