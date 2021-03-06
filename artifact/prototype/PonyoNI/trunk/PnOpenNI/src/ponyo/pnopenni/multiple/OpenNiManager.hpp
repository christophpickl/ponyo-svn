#pragma once
#ifndef OPENNIMANAGER_HPP_
#define OPENNIMANAGER_HPP_

#include <ponyo/pnopenni/Cam.hpp>
#include <ponyo/pnopenni/CamInitDescriptor.hpp>
#include <ponyo/pnopenni/multiple/CamInitializer.hpp>
#include <ponyo/pnopenni/multiple/CamCalibrator.hpp>

namespace pn {
class OpenNiManager : public CamInitializerListener {
public:
	OpenNiManager(CamInitializer*, CamCalibrator*, UserManager*);
	virtual ~OpenNiManager();

	void init() throw (OpenNiException);
	void listDevices(CamInitDescriptor*); // TODO rename to loadDevices()
	void startAll();
	void calibrate();
	void shutdown();

	/** implements CamInitializerListener */
	void onInitializedCams(std::vector<Cam*>);
	/** implements CamInitializerListener */
	void onException(Exception&);

private:
	xn::Context context;
	CamInitializer* camInitializer;
	CamCalibrator* camCalibrator;
	UserManager* userManager;

	/*transient*/ CamInitDescriptor* initDescriptor;

	static Log* LOG;
	std::vector<Cam*> cams;
};
}

#endif // OPENNIMANAGER_HPP_
