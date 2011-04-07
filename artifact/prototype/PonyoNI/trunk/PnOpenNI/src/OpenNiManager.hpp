#pragma once
#ifndef OPENNIMANAGER_HPP_
#define OPENNIMANAGER_HPP_

#include "common_openni.hpp"
#include "CamInitializer.hpp"
#include "ImageSaver.hpp"

namespace pn {
class OpenNiManager : public CamInitializerListener {
public:
	OpenNiManager(CamInitializer*, ImageSaver*);
	virtual ~OpenNiManager();

	void init() throw (OpenNiException);
	void listDevices();
	void startGenerateImageForAllCams();
	void createImageForAllCams();
	void shutdown();

	/** implements CamInitializerListener */
	void onInitializedCams(std::vector<Cam*>);

private:
	CamInitializer* camInitializer;
	ImageSaver* imageSaver;
	xn::Context context;
	static Log* LOG;
	std::vector<Cam*> cams;
};
}

#endif // OPENNIMANAGER_HPP_
