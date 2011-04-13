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
	OpenNIFacade();
	virtual ~OpenNIFacade();

//	void registerUserStateCallback(UserStateCallback);
//	void registerJointPositionCallback(JointPositionCallback);

	void startWithXml(const char* configPath, UserStateCallback, JointPositionCallback) throw(OpenNiException);
	void startRecording(const char* oniFilePath, UserStateCallback, JointPositionCallback) throw(OpenNiException);

	void shutdown();

private:
	static Log* LOG;

	UserManager* userManager;
	UpdateThread<OpenNIFacade> updateThread;
	xn::Context context;
	xn::DepthGenerator depthGenerator;

	xn::DepthMetaData depthMetaData; // TODO this seems like a hack, but is mandatory, as otherwise something like: ...
	// usergenerator does not get data from its dependent depthgenerator (?!)

	void internalSetup(UserStateCallback, JointPositionCallback) throw(OpenNiException);
	void onUpdateThread();

};
}
#endif // OpenNIFacade_HPP_
