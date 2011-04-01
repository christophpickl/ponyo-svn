
#include <time.h>
#include "ImageSaver.h"

ImageSaver::ImageSaver() {
	printf("new ImageSaver()\n");
}

ImageSaver::~ImageSaver() {
	printf("delete ~ImageSaver()\n");
}

void ImageSaver::saveToDefault(const xn::ImageMetaData& image, const std::string& fileNameSuffix) {
	printf("ImageSaver.saveToDefault(image, fileNameSuffix)\n");
	int num = time(NULL);
	char framenumber[10];
	sprintf(framenumber,"%06d", num);
	std::stringstream ss;
	std::string str_frame_number;
	ss << framenumber;

	ss >> str_frame_number;
	std::string targetFileName = "CapturedFrames/image_RGB_"+ str_frame_number +"-" + fileNameSuffix + ".jpg";

	this->saveToFile(image, targetFileName);
}

void ImageSaver::saveToFile(const xn::ImageMetaData& image, const std::string& targetFileName) {
	printf("ImageSaver.saveToFile(image, targetFileName=%s)\n", targetFileName.c_str());

	const XnRGB24Pixel* currentImageRow = image.RGB24Data();
	const XnRGB24Pixel* currentPixel;
	const int xResolution = image.XRes();
	const int yResolution = image.YRes();

	cv::Mat colorArray[3];
	colorArray[0] = cv::Mat(yResolution, xResolution, CV_8U);
	colorArray[1] = cv::Mat(yResolution, xResolution, CV_8U);
	colorArray[2] = cv::Mat(yResolution, xResolution, CV_8U);

	for (int y = 0; y < yResolution; y++)  {
		uchar* blueValue = colorArray[0].ptr<uchar>(y);
		uchar* greenValue = colorArray[1].ptr<uchar>(y);
		uchar* redValue = colorArray[2].ptr<uchar>(y);

		currentPixel = currentImageRow;
		for (int x = 0; x < xResolution; x++)  {
			blueValue[x] = currentPixel->nBlue;
			greenValue[x] = currentPixel->nGreen;
			redValue[x] = currentPixel->nRed;

			currentPixel++;
		}

		currentImageRow += xResolution;
	}

	cv::Mat colorImage;
	cv::merge(colorArray, 3, colorImage);

//    IplImage bgrIpl = colorImage; // create a IplImage header for the cv::Mat bgrImage ==> ??? WHAT FOR ???
    // bool imwrite(const string& filename, const Mat& img, const vector<int>& params=vector<int>()) ... see: http://opencv.willowgarage.com/documentation/cpp/reading_and_writing_images_and_video.html#cv-imwrite
    cv::imwrite(targetFileName, colorImage);
//    cvSaveImage(targetFileNameChars, &bgrIpl);
}
