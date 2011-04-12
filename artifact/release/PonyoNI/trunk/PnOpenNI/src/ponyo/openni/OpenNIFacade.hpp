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
	 * @param JointDataCallback non-null function handle
	 */
	OpenNIFacade(UserStateCallback userStateCallback, JointDataCallback jointDataCallback);
	virtual ~OpenNIFacade();

	void startWithXml(const char* configPath) throw(OpenNiException);
	void startRecording(const char* oniFilePath) throw(OpenNiException);
	void destroy();

private:
	static Log* LOG;

	UserManager* userManager;
	UpdateThread* updateThread;
	xn::Context context;
	xn::DepthGenerator depthGenerator;

	void internalSetup() throw(OpenNiException);

};
}
#endif // OpenNIFacade_HPP_
