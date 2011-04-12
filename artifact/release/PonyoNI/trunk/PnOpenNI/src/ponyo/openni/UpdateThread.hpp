#pragma once
#ifndef UPDATETHREAD_HPP_
#define UPDATETHREAD_HPP_

#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include <ponyo/openni/pnopenni_inc.hpp>
#include <ponyo/openni/UserManager.hpp>

namespace pn {

template <class CallbackType>
class UpdateThread {
public:
	typedef void (CallbackType::*aFunction) ();

	UpdateThread(CallbackType* callbackInstance,  aFunction callbackMethod);
	virtual ~UpdateThread();

	void start(xn::Context&);
	void stopAndJoin();
private:
	static Log* LOG;

	CallbackType* callbackInstance;
	aFunction callbackMethod;

	boost::thread updateThread;
	bool threadShouldRun;


	void onThreadRun();
};
}

#endif // UPDATETHREAD_HPP_
