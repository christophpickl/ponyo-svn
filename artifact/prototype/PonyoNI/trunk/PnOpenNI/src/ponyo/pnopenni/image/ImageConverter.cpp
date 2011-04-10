#include <ponyo/pnopenni/image/ImageConverter.hpp>

// FIXME merge ImageConverter/Detector/Saver to single class

namespace pn {

Log* ImageConverter::LOG = NEW_LOG(__FILE__)

ImageConverter::ImageConverter() {
	LOG->debug("new ImageConverter()");
}

ImageConverter::~ImageConverter() {
	LOG->debug("~ImageConverter()");
}

void ImageConverter::convertMeta2Mat(cv::Mat& targetMat, const xn::ImageMetaData* sourceMeta) {
	const XnRGB24Pixel* currentImageRow = sourceMeta->RGB24Data();
	const XnRGB24Pixel* currentPixel;
	const int xResolution = sourceMeta->XRes();
	const int yResolution = sourceMeta->YRes();

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
	cv::merge(colorArray, 3, targetMat);
}

}
