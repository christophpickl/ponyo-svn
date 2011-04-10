#pragma once
#ifndef IMAGEDETECTOR_HPP_
#define IMAGEDETECTOR_HPP_

#include <ponyo/pncommon/pninclude_opencv.h>
#include <ponyo/pnopenni/image/ImageDetectorException.hpp>

namespace pn {
class ImageDetector {
public:
	ImageDetector();
	virtual ~ImageDetector();

	cv::Point match(cv::Mat&, cv::Mat&) throw(ImageDetectorException);
private:
	static Log* LOG;
	static int MATCH_TEMPLATE_METHOD;
};
}

#endif // IMAGEDETECTOR_HPP_
