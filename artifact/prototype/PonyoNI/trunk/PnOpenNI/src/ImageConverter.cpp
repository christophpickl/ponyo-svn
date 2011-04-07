#include "ImageConverter.hpp"

namespace pn {

Log* ImageConverter::LOG = NEW_LOG(__FILE__)

ImageConverter::ImageConverter() {
	LOG->debug("new ImageConverter()");
}

ImageConverter::~ImageConverter() {
	LOG->debug("~ImageConverter()");
}

void ImageConverter::convertImageMetaData2cvMat(cv::Mat& targetMat, const xn::ImageMetaData* sourceData) {
	LOG->debug("convertImageMetaData2cvMat(..)");


}

}
