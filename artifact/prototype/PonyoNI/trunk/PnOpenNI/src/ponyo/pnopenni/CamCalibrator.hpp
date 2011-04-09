#pragma once
#ifndef CAMCALIBRATOR_HPP_
#define CAMCALIBRATOR_HPP_

#include <ponyo/pncommon/pninclude_opencv.h>
#include <ponyo/pnopenni/common_openni.hpp>

#include <ponyo/pnopenni/Cam.hpp>
#include <ponyo/pnopenni/ImageDetector.hpp>
#include <ponyo/pnopenni/ImageConverter.hpp>
#include <ponyo/pnopenni/ImageSaver.hpp>

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
