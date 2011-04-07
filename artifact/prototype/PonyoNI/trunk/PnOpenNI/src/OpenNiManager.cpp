#include <stdio.h>
#include <iostream>

#include "OpenNiManager.hpp"

namespace pn {

Log* OpenNiManager::LOG = NEW_LOG(__FILE__)

OpenNiManager::OpenNiManager(
		CamInitializer* pCamInitializer,
		CamCalibrator* pCamCalibrator,
		UserManager* pUserManager
		) :
		camInitializer(pCamInitializer),
		camCalibrator(pCamCalibrator),
		userManager(pUserManager)
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

void OpenNiManager::listDevices(CamInitDescriptor* initDescriptor) { // TODO throw (ConnectionException) {
	// TODO if(!initedYet) throw IllegalStateException
	LOG->info("listDevices(initDescriptor)");

	LOG->trace("listDevices # 1/3");
	this->initDescriptor = initDescriptor;
	LOG->trace("listDevices # 2/3");
	this->camInitializer->fetchDevices(this->context, this->initDescriptor);
	LOG->trace("listDevices # 3/3");
}

/**
 * Implements CamInitializerListener callback.
 */
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

/**
 * Implements CamInitializerListener callback.
 */
void OpenNiManager::onException(Exception& ex) {
	LOG->error("onException(ex)");
	fprintf(stderr, "Exception: %s\n", ex.getMessage());
	ex.printBacktrace();
}

void OpenNiManager::startAll() {
	LOG->info("startAll()");

	for(int i = 0, n = this->cams.size(); i < n; i++) {
		Cam* currentCam = cams.at(i);
		std::cout << (i+1) << ". " << currentCam->toString() << std::endl;

		if(this->initDescriptor->isImageGeneratorRequired()) {
			xn::ImageGenerator generator = currentCam->getImageGenerator();
			CHECK_RC(generator.StartGenerating(), "generator.StartGenerating()");
		}

		if(this->initDescriptor->isUserGeneratorRequired()) {
			this->userManager->start();
		}
	}
}

void OpenNiManager::calibrate() {
	LOG->info("calibrate()");

	std::vector<cv::Point3d*> calibrationDeltas;
	bool calibrationSuccessful = this->camCalibrator->calibrate(this->cams, calibrationDeltas);
	if(calibrationSuccessful == false) {
		LOG->warn("Calibration failed!");
		return;
	}

	printf("OpenNiManager ... Calibration points:\n\n");
	for(int i=0, n=calibrationDeltas.size(); i < n; i++) {
		cv::Point3d* p3 = calibrationDeltas.at(i);
		printf("\t%i. delta: %.2f x %.2f x %.2f\n", (i+1), p3->x, p3->y, p3->z);
	}
}

void OpenNiManager::shutdown() {
	LOG->info("shutdown()");

	for(int i = 0, n = this->cams.size(); i < n; i++) {
		Cam* currentCam = cams.at(i);

		if(this->initDescriptor->isImageGeneratorRequired()) {
			xn::ImageGenerator generator = currentCam->getImageGenerator();
			if(generator.IsGenerating()) {
				printf("stop generating cam %s\n", currentCam->toString().c_str());
				generator.StopGenerating();
				// TODO "SOFT"_CHECK_RC(generator.StopGenerating(), "generator.StopGenerating()");
			}
		}

		if(this->initDescriptor->isUserGeneratorRequired()) {
			this->userManager->stop();
		}
	}

	this->context.Shutdown();
}

}
