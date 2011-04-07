#pragma once
#ifndef OPENNIMANAGER_HPP_
#define OPENNIMANAGER_HPP_

#include "common_openni.hpp"
#include "CamInitializer.hpp"
#include "CamCalibrator.hpp"

namespace pn {
class OpenNiManager : public CamInitializerListener {
public:
	OpenNiManager(CamInitializer*, CamCalibrator*);
	virtual ~OpenNiManager();

	void init() throw (OpenNiException);
	void listDevices(); // TODO rename to loadDevices()
	void startGenerateImageForAllCams();
	void calibrate();
	void shutdown();

	/** implements CamInitializerListener */
	void onInitializedCams(std::vector<Cam*>);

private:
	xn::Context context;
	CamInitializer* camInitializer;
	CamCalibrator* camCalibrator;

	static Log* LOG;
	std::vector<Cam*> cams;
};
}

#endif // OPENNIMANAGER_HPP_
