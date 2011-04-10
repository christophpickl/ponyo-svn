#pragma once
#ifndef CONTEXTX_HPP_
#define CONTEXTX_HPP_

#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include <ponyo/pnopenni/common_openni.hpp>
#include <ponyo/pnopenni/UserManager.hpp>

namespace pn {
class ContextX {
public:
	ContextX();
	~ContextX();
	void init() throw(OpenNiException);
	void start() throw(OpenNiException);
	void waitAndUpdate();
	void shutdown();
	void addUserManagerListener(UserManagerListener*);
private:
	static Log* LOG;
	xn::Context context;
	UserManager* userManager;
	XnMapOutputMode mapMode;
	xn::DepthGenerator depthGenerator;
	boost::thread updateThread;
	bool threadShouldRun;

	void onThreadRun();
	// TODO block assign-operator and copy-ctor
};
}

#endif // CONTEXTX_HPP_
