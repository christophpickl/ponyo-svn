#include <stdio.h>
#include <iostream>

#include "OpenNiManager.hpp"

namespace pn {

Log* OpenNiManager::LOG = NEW_LOG(__FILE__)

OpenNiManager::OpenNiManager(
		CamInitializer* pCamInitializer,
		CamCalibrator* pCamCalibrator
		) :
		camInitializer(pCamInitializer),
		camCalibrator(pCamCalibrator)
{

	LOG->debug("new OpenNiManager()");
	this->camInitializer->addListener(this);
}

OpenNiManager::~OpenNiManager() {
	LOG->debug("~OpenNiManager()");
	this->camInitializer->removeListener(this);
}

void OpenNiManager::init() throw (OpenNiException) {
	LOG->info("init()");

	CHECK_RC(this->context.Init(), "context.Init()");
}

void OpenNiManager::listDevices() { // TODO throw (ConnectionException) {
	// TODO if(!initedYet) throw IllegalStateException
	LOG->info("listDevices()");
	this->camInitializer->fetchDevices(this->context);
}

void OpenNiManager::onInitializedCams(std::vector<Cam*> cams) {
	LOG->info("onInitializedCams(cams)");
	// TODO delete this->cams; ??
	this->cams = cams;

	const int n = (int) (int) cams.size();
	printf("Successfully found %i cam(s):\n--------------------------\n\n", n);
	for(int i = 0; i < n; i++) {
		Cam* currentCam = cams.at(i);
		std::cout << "\t" << (i+1) << ". " << currentCam->toString() << std::endl;
	}
	printf("\n");
}

void OpenNiManager::startGenerateImageForAllCams() {
	LOG->info("startGenerateImageForAllCams()");

	for(int i = 0, n = this->cams.size(); i < n; i++) {
		Cam* currentCam = cams.at(i);
		std::cout << (i+1) << ". " << currentCam->toString() << std::endl;
		xn::ImageGenerator generator = currentCam->getImageGenerator();
		CHECK_RC(generator.StartGenerating(), "generator.StartGenerating()");
	}
}
void OpenNiManager::calibrate() {
	LOG->info("calibrate()");

	std::vector<cv::Point*> foundTemplatePositions;
	bool calibrationSuccessful = this->camCalibrator->calibrate(this->cams, foundTemplatePositions);
	if(calibrationSuccessful == false) {
		return;
	}

	printf("OpenNiManager ... Calibration points:\n\n");
	for(int i=0, n=foundTemplatePositions.size(); i < n; i++) {
		cv::Point* p = foundTemplatePositions.at(i);
		printf("\t%i. point: %ix%i\n", (i+1), p->x, p->y);
	}
}

void OpenNiManager::shutdown() {
	LOG->info("shutdown()");

	for(int i = 0, n = this->cams.size(); i < n; i++) {
		Cam* currentCam = cams.at(i);
		xn::ImageGenerator generator = currentCam->getImageGenerator();
		if(generator.IsGenerating()) {
			printf("stop generating cam %s\n", currentCam->toString().c_str());
			generator.StopGenerating();
//			TODO "SOFT"_CHECK_RC(generator.StopGenerating(), "generator.StopGenerating()");
		}
	}

	this->context.Shutdown();
}

}
