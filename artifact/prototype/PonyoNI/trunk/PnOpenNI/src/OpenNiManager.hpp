#pragma once
#ifndef OPENNIMANAGER_HPP_
#define OPENNIMANAGER_HPP_

#include "common_openni.hpp"
#include "CamInitializer.hpp"

namespace pn {
class OpenNiManager : public CamInitializerListener {
public:
	OpenNiManager(CamInitializer*);
	virtual ~OpenNiManager();

	void init() throw (OpenNiException);
	void listDevices();
	void startGenerateImageForAllCams();
	void shutdown();

	/** implements CamInitializerListener */
	void onInitializedCams(std::vector<Cam*>);

private:
	CamInitializer* initializer;
	xn::Context context;
	static Log* LOG;
	std::vector<Cam*> cams;
};
}

#endif // OPENNIMANAGER_HPP_
