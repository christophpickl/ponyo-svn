#pragma once
#ifndef OpenNIFacade_HPP_
#define OpenNIFacade_HPP_

#include <ponyo/openni/pnopenni_inc.hpp>
#include <ponyo/openni/UpdateThread.hpp>
#include <ponyo/openni/includes/headers_config.hpp>
#include <ponyo/openni/logic/user/UserManager.hpp>
#include <ponyo/openni/logic/image/ImagaManager.hpp>

namespace pn {


class OpenNIFacade {

public:
	OpenNIFacade();
	virtual ~OpenNIFacade();

//	TODO void start(StartConfig& config) throw(OpenNiException);
	void startWithXml(StartXmlConfig& config) throw(OpenNiException);
	void startRecording(StartOniConfig& config) throw(OpenNiException);

	void toggleMirror() throw (OpenNiException);
	void setWindowVisible(bool visible);
	bool isWindowVisible();

	void shutdown();

private:
	static Log* LOG;

	xn::Context context;
	UpdateThread<OpenNIFacade> updateThread;

	xn::ImageGenerator* imageGenerator;
	xn::DepthGenerator* depthGenerator;

	UserManager* userManager;
	/** Depending on start configuration, will be (un)used */
	ImagaManager* imageManager;

	xn::DepthMetaData depthMetaData; // TODO this seems like a hack, but is mandatory, as otherwise something like: ...
	// usergenerator does not get data from its dependent depthgenerator (?!)

	void internalSetup(GenericConfig& config) throw(OpenNiException);
	void onUpdateThread();

};
}
#endif // OpenNIFacade_HPP_
