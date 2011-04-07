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

	void calibrate(std::vector<Cam*>& cams);
private:
	static Log* LOG;

	ImageDetector* detector;
	ImageConverter* converter;
	ImageSaver* saver;
	cv::Mat* templateImage;

};
}

#endif // CAMCALIBRATOR_HPP_
