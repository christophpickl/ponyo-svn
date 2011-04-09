#include "ImageDetector.hpp"

namespace pn {

Log* ImageDetector::LOG = NEW_LOG(__FILE__)

int ImageDetector::MATCH_TEMPLATE_METHOD = CV_TM_SQDIFF;
ImageDetector::ImageDetector() {
	LOG->debug("new ImageDetector()");
}

ImageDetector::~ImageDetector() {
	LOG->debug("~ImageDetector()");
}
cv::Point ImageDetector::match(cv::Mat& sourceImage, cv::Mat& templateImage) throw(ImageDetectorException) {
	LOG->debug("match(..)");

	cv::Mat matchResult;
	try {
		cv::matchTemplate(sourceImage, templateImage, matchResult, ImageDetector::MATCH_TEMPLATE_METHOD);
	} catch(cv::Exception& ex) {
		fprintf(stderr, "Internal exception: %s\n", ex.what());
		throw ImageDetectorException("cv::matchTemplate() failed!", AT);
	}

	double minValue;
	double maxValue;
	cv::Point minLoc;
	cv::Point maxLoc;
	try {
		// finds global minimum and maximum array elements and returns their values and their locations
		cv::minMaxLoc(matchResult, &minValue, &maxValue, &minLoc, &maxLoc/*, const Mat& mask=Mat()*/);
//		printf("ImageDetector ... minMaxLoc result:\n\t- minValue: %f\n\t- maxValue: %f\n\t- minLoc: %ix%i\n\t- maxLoc: %ix%i\n\n",
//				minValue, maxValue, minLoc.x, minLoc.y, maxLoc.x, maxLoc.y);
	} catch(cv::Exception& ex) {
		fprintf(stderr, "Internal exception: %s\n", ex.what());
		throw ImageDetectorException("cv::minMaxLoc() failed!", AT);
	}

	return minLoc;
//	if(matchTemplateMethod == CV_TM_CCORR || matchTemplateMethod == CV_TM_CCOEFF) return maxLoc;
}
}
