#include <stdio.h>
#include <iostream>

#include "OpenNiManager.hpp"

namespace pn {

Log* OpenNiManager::LOG = NEW_LOG(__FILE__)

OpenNiManager::OpenNiManager(CamInitializer* pCamInitializer, ImageSaver* pImageSaver) :
		camInitializer(pCamInitializer),
		imageSaver(pImageSaver) {

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
	printf("cams.size=%i\n", n);
	for(int i = 0; i < n; i++) {
		Cam* currentCam = cams.at(i);
		std::cout << (i+1) << ". " << currentCam->toString() << std::endl;
	}
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
void OpenNiManager::createImageForAllCams() {
	LOG->info("createImageForAllCams()");

	for(int i = 0, n = this->cams.size(); i < n; i++) {
		Cam* currentCam = cams.at(i);
		const xn::ImageMetaData* imageData = currentCam->getRecentImageData();
		this->imageSaver->saveToDefault(imageData, currentCam->getCleanId());
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
