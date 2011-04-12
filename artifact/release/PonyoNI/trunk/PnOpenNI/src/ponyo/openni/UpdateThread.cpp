#include <ponyo/openni/UpdateThread.hpp>

namespace pn {

Log* UpdateThread<CallbackType>::LOG = NEW_LOG();

UpdateThread::UpdateThread(CallbackType* pCallbackInstance,  aFunction pCallbackMethod) :
		callbackInstance(pCallbackInstance),
		callbackMethod(pCallbackMethod),
		threadShouldRun(true) {
	LOG->debug("new UpdateThread(callbackInstance, callbackMethod)");
}

UpdateThread<CallbackType>::~UpdateThread() {
	LOG->debug("~UpdateThread()");
}

void UpdateThread::start(xn::Context& context) {
	LOG->debug("start(context) ... spawning update thread");
	this->updateThread = boost::thread(&UpdateThread::onThreadRun, this, context);
}

void UpdateThread::onThreadRun(xn::Context& context) {
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

void UpdateThread::stopAndJoin() {
	LOG->debug("stopAndJoin() START");
	this->threadShouldRun = false;
	this->updateThread.join();
	LOG->debug("stopAndJoin() END");
}

}
