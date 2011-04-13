#pragma once
#ifndef OpenNIFacade_HPP_
#define OpenNIFacade_HPP_

#include <ponyo/openni/pnopenni_inc.hpp>
#include <ponyo/openni/UserManager.hpp>
#include <ponyo/openni/UpdateThread.hpp>

namespace pn {


class OpenNIFacade {

public:
	/**
	 * @param userStateCallback non-null function handle
	 * @param jointPositionCallback non-null function handle
	 */
	OpenNIFacade(UserStateCallback userStateCallback, JointPositionCallback jointPositionCallback);
	virtual ~OpenNIFacade();

	void startWithXml(const char* configPath) throw(OpenNiException);
	void startRecording(const char* oniFilePath) throw(OpenNiException);
	void destroy();

private:
	static Log* LOG;

	UserManager* userManager;
	UpdateThread<OpenNIFacade>* updateThread;
	xn::Context context;
	xn::DepthGenerator depthGenerator;

	xn::DepthMetaData depthMetaData; // TODO this seems like a hack, but is mandatory, as otherwise something like: ...
	// usergenerator does not get data from its dependent depthgenerator (?!)

	void internalSetup() throw(OpenNiException);
	void onUpdateThreadGotData();

};
}
#endif // OpenNIFacade_HPP_
