#pragma once
#ifndef CAMCALIBRATOR_HPP_
#define CAMCALIBRATOR_HPP_

#include "common_openni.hpp"
#include "pninclude_opencv.h"

#include "Cam.hpp"
#include "ImageDetector.hpp"
#include "ImageConverter.hpp"
#include "ImageSaver.hpp"

namespace pn {
class CamCalibrator {
public:
	CamCalibrator(ImageDetector*, ImageConverter*, ImageSaver*, cv::Mat* templateImage);
	virtual ~CamCalibrator();

	bool calibrate(std::vector<Cam*>& cams, std::vector<cv::Point3d*>& calibrationDeltas);
private:
	static Log* LOG;

	ImageDetector* detector;
	ImageConverter* converter;
	ImageSaver* saver;
	cv::Mat* templateImage;

	bool isValidDetectionPoint(cv::Point&);

};
}

#endif // CAMCALIBRATOR_HPP_
