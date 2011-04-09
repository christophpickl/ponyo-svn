//#include <time.h>
#include "ImageSaver.hpp"

namespace pn {

Log* ImageSaver::LOG = NEW_LOG(__FILE__)

ImageSaver::ImageSaver(ImageConverter* pConverter) : converter(pConverter) {
	LOG->debug("new ImageSaver(converter)");
}

ImageSaver::~ImageSaver() {
	LOG->debug("~ImageSaver()");
}

void ImageSaver::saveToDefault(const xn::ImageMetaData* image, const std::string& fileNameSuffix) {
	LOG->debug("ImageSaver.saveToDefault(image, fileNameSuffix)");
	int num = time(NULL);
	char framenumber[10];
	sprintf(framenumber,"%06d", num);
	std::stringstream ss;
	ss << framenumber;

//	std::string str_frame_number;
//	ss >> str_frame_number;
	std::string str_frame_number = ss.str();

	std::string targetFileName = "CapturedFrames/image_RGB_"+ str_frame_number +"-" + fileNameSuffix + ".jpg";

	this->saveToFile(image, targetFileName);
}

void ImageSaver::saveToFile(const xn::ImageMetaData* imageMetaData, const std::string& targetFileName) {
	LOG->debug("saveToFile(image, targetFileName)");

	cv::Mat colorImage;
	this->converter->convertMeta2Mat(colorImage, imageMetaData);

//    IplImage bgrIpl = colorImage; // create a IplImage header for the cv::Mat bgrImage ==> ??? WHAT FOR ???
    // bool imwrite(const string& filename, const Mat& img, const vector<int>& params=vector<int>()) ... see: http://opencv.willowgarage.com/documentation/cpp/reading_and_writing_images_and_video.html#cv-imwrite

    cv::imwrite(targetFileName, colorImage);
//    cvSaveImage(targetFileNameChars, &bgrIpl);
}

}
