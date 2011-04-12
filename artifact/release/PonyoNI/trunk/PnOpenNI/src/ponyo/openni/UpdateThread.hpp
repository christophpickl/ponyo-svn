#pragma once
#ifndef UPDATETHREAD_HPP_
#define UPDATETHREAD_HPP_

#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include <ponyo/openni/pnopenni_inc.hpp>
#include <ponyo/openni/UserManager.hpp>

namespace pn {
class UpdateThread {
public:
	UpdateThread(xn::Context&, xn::DepthGenerator&, UserManager*);
	virtual ~UpdateThread();

	void start();
	void stopAndJoin();
private:
	static Log* LOG;

	boost::thread updateThread;
	xn::Context context;
	xn::DepthGenerator depthGenerator;
	UserManager* userManager;
	bool threadShouldRun;


	void onThreadRun();
};
}

#endif // UPDATETHREAD_HPP_
