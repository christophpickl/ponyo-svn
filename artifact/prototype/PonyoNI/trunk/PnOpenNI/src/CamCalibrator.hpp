#pragma once
#ifndef CAMCALIBRATOR_HPP_
#define CAMCALIBRATOR_HPP_

#include "common_openni.hpp"
#include "pninclude_opencv.h"

#include "Cam.hpp"
#include "ImageDetector.hpp"
#include "ImageSaver.hpp"

namespace pn {
class CamCalibrator {
public:
	CamCalibrator(ImageDetector* pDetector, ImageSaver* pSaver); //, cv::Mat* pTemplateImage);
	virtual ~CamCalibrator();

	void calibrate(std::vector<Cam*>& cams);
private:
	static Log* LOG;

	ImageDetector* detector;
	ImageSaver* saver;
//	cv::Mat* templateImage;

};
}

#endif // CAMCALIBRATOR_HPP_
