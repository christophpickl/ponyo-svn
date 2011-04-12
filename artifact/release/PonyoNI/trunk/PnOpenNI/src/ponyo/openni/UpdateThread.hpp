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

	UpdateThread(CallbackType* pCallbackInstance,  aFunction pCallbackMethod) :
		callbackInstance(pCallbackInstance),
		callbackMethod(pCallbackMethod),
		threadShouldRun(true)
	{
		LOG = NEW_LOG();
//		Log* UpdateThread<CallbackType>::LOG = ;
		LOG->debug("new UpdateThread(callbackInstance, callbackMethod)");
	}

	~UpdateThread() {
		LOG->debug("~UpdateThread()");
	}

	void start(xn::Context& context) {
		LOG->debug("start(context) ... spawning update thread");
		this->updateThread = boost::thread(&UpdateThread::onThreadRun, this, context);
	}

	void stopAndJoin() {
		LOG->debug("stopAndJoin() START");
		this->threadShouldRun = false;
		this->updateThread.join();
		LOG->debug("stopAndJoin() END");
	}

private:
	Log* LOG;

	CallbackType* callbackInstance;
	aFunction callbackMethod;

	boost::thread updateThread;
	bool threadShouldRun;


	void onThreadRun(xn::Context& context) {
		LOG->info("onThreadRun(context) START");

	//	try {
			while(this->threadShouldRun) {
				context.WaitAnyUpdateAll();
				(this->callbackInstance->*this->callbackMethod)();
			}
	//	} catch(Exception& e) {
	//		TODO this->broadcastThreadException(e);
	//	}
		LOG->info("onThreadRun() END");
	}
};
}

#endif // UPDATETHREAD_HPP_
