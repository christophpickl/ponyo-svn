#include <stdio.h>
#include <ponyo/pnopenni/CamCalibrator.hpp>

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

inline bool CamCalibrator::isValidDetectionPoint(cv::Point& p) {
	return p.x > 30 && p.x < 450; // opencv returns coordinates near edges if object not found
}

bool CamCalibrator::calibrate(std::vector<Cam*>& cams, std::vector<cv::Point3d*>& foundTemplatePositions) {
	LOG->debug("calibrate(cams)");

	const int n = cams.size();

	std::vector<cv::Point> foundObjectPositions;
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

		if(!this->isValidDetectionPoint(p) && calibrationSuccessful == true) { // haha, nice try; but it works ;)
			calibrationSuccessful = false;
//			printf("Calibration FAILED!\n");
		}
		foundObjectPositions.push_back(p);
		// additionally save image to disk
//		this->saver->saveToDefault(sourceMeta, currentCam->getCleanId());
	}

	if(calibrationSuccessful == false) {
		return false;
	}

	for(int i = 0; i < n; i++) {
		if(i == 0) {
			// first cam is implicitly main cam => zero-delta should be applied
			foundTemplatePositions.push_back(new cv::Point3d(0, 0, 0));
			continue;
		}
		cv::Point pivot = foundObjectPositions.at(0);
		Cam* cam = cams.at(i);
//		cam->getRecentDepthData();
		double pivotZ = 0.3; // FIXME get depth map
		double pZ = 0.3;

		cv::Point p = foundObjectPositions.at(i);
		double xDelta = p.x - pivot.x;
		double yDelta = p.y - pivot.y;
		double zDelta = pZ  - pivotZ;

		cv::Point3d* p3Delta = new cv::Point3d(xDelta, yDelta, zDelta);
		foundTemplatePositions.push_back(p3Delta);
	}

	// assert(cams.size == foundTemplatePositions.size)
	return calibrationSuccessful;
}

}
