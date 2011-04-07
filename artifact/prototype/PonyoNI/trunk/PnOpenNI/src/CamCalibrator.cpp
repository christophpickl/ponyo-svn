#include "CamCalibrator.hpp"

namespace pn {

Log* CamCalibrator::LOG = NEW_LOG(__FILE__)

CamCalibrator::CamCalibrator(ImageDetector* pDetector, ImageSaver* pSaver) : //, cv::Mat* pTemplateImage) :
		detector(pDetector), saver(pSaver) { //, templateImage(pTemplateImage) {
	LOG->debug("new CamCalibrator(detector, saver, pTemplateImage)");
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
		const xn::ImageMetaData* imageData = currentCam->getRecentImageData();
		// FIXME convert ImageMetaData to Mat
		cv::Mat sourceImage;

//		cv::Point p = this->detector->match(sourceImage, *this->templateImage);
//		templateCoordinates[i] = p;
//		int x = p.x;
//		int x = p.y;

		// additionally save image to disk
//		this->saver->saveToDefault(imageData, currentCam->getCleanId());
	}
	// TODO create own class CamCalibrator

	delete[] templateCoordinates;
}

}
