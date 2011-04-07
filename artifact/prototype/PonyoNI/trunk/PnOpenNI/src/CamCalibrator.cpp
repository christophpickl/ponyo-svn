#include <stdio.h>
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
bool CamCalibrator::calibrate(std::vector<Cam*>& cams, std::vector<cv::Point*>& foundTemplatePositions) {
	LOG->debug("calibrate(cams)");

	const int n = cams.size();

	bool calibrationSuccessful = true;
	for(int i = 0; i < n; i++) {
		printf("calibrating cam number %i\n", i);
		Cam* currentCam = cams.at(i);

		// TODO mutex lock? or maybe even copy, as calibrate is only invoked once (ar at least not every frame)
		const xn::ImageMetaData* sourceMeta = currentCam->getRecentImageData();

		cv::Mat sourceMat;
		this->converter->convertMeta2Mat(sourceMat, sourceMeta);

		cv::Point p = this->detector->match(sourceMat, *this->templateImage);
		printf("CamCalibrator ... camera %i# (%s) match template point: %ix%i\n", i, currentCam->getCleanId().c_str(), p.x, p.y);

		if(p.x < 30 && calibrationSuccessful == true) { // haha, nice try; but it works ;)
			calibrationSuccessful = false;
			printf("Calibration FAILED!\n");
		}
		foundTemplatePositions.push_back(new cv::Point(p));

		// additionally save image to disk
		this->saver->saveToDefault(sourceMeta, currentCam->getCleanId());
	}

	// assert(cams.size == foundTemplatePositions.size)
	return calibrationSuccessful;
}

}
