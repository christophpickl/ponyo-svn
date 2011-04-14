#pragma once
#ifndef OpenNIFacade_HPP_
#define OpenNIFacade_HPP_

#include <ponyo/openni/pnopenni_inc.hpp>
#include <ponyo/openni/UserManager.hpp>
#include <ponyo/openni/UpdateThread.hpp>
#include <ponyo/openni/UpdateThread.hpp>
#include <ponyo/openni/StartConfigurations.hpp>

namespace pn {


class OpenNIFacade {

public:
	OpenNIFacade();
	virtual ~OpenNIFacade();

//	TODO void start(StartConfiguration& configuration) throw(OpenNiException);
	void startWithXml(StartXmlConfiguration& configuration) throw(OpenNiException);
	void startRecording(StartOniConfiguration& configuration) throw(OpenNiException);

	void shutdown();

private:
	static Log* LOG;

	UserManager* userManager;
	UpdateThread<OpenNIFacade> updateThread;
	xn::Context context;
	xn::DepthGenerator depthGenerator;

	xn::DepthMetaData depthMetaData; // TODO this seems like a hack, but is mandatory, as otherwise something like: ...
	// usergenerator does not get data from its dependent depthgenerator (?!)

	void internalSetup(AbstractConfiguration& configuration) throw(OpenNiException);
	void onUpdateThread();

};
}
#endif // OpenNIFacade_HPP_
