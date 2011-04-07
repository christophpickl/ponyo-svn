#pragma once
#ifndef IMAGEDETECTOR_HPP_
#define IMAGEDETECTOR_HPP_

#include "common_openni.hpp"
#include "pninclude_opencv.h"
#include "ImageDetectorException.hpp"

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
