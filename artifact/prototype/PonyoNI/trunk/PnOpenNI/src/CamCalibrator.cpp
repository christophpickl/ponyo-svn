#include "CamCalibrator.hpp"

namespace pn {

Log* CamCalibrator::LOG = NEW_LOG(__FILE__)

CamCalibrator::CamCalibrator(
		ImageDetector* pDetector,
		ImageConverter* pConverter,
		ImageSaver* pSaver,
		cv::Mat* pTemplateImage
		) :
			detector(pDetector),
			converter(pConverter),
			saver(pSaver),
			templateImage(pTemplateImage)
{
	LOG->debug("new CamCalibrator(detector, converter saver, templateImage)");
}

CamCalibrator::~CamCalibrator() {
	LOG->debug("~CamCalibrator()");

}
void CamCalibrator::calibrate(std::vector<Cam*>& cams) {
	LOG->debug("calibrate(cams)");

	const int n = cams.size();
	cv::Point* templateCoordinates = new cv::Point[n];

	for(int i = 0; i < n; i++) {
		printf("calibrating cam number %i\n", i);
		Cam* currentCam = cams.at(i);

		// TODO mutex lock? or maybe even copy, as calibrate is only invoked once (ar at least not every frame)
		const xn::ImageMetaData* imageData = currentCam->getRecentImageData();

		cv::Mat sourceImage;
		this->converter->convertImageMetaData2cvMat(sourceImage, imageData);

		cv::Point p = this->detector->match(sourceImage, *this->templateImage);
		templateCoordinates[i] = p;

		// additionally save image to disk
		this->saver->saveToDefault(imageData, currentCam->getCleanId());
	}
	// TODO create own class CamCalibrator

	delete[] templateCoordinates;
}

}
