#include <ponyo/openni/UpdateThread.hpp>

namespace pn {

Log* UpdateThread::LOG = NEW_LOG();

UpdateThread::UpdateThread(xn::Context& pContext, xn::DepthGenerator& pDepthGenerator, UserManager* pUserManager) :
		threadShouldRun(true), context(pContext), userManager(pUserManager), depthGenerator(pDepthGenerator) {
	LOG->debug("new UpdateThread(context, userManager, depthGenerator)");
}

UpdateThread::~UpdateThread() {
	LOG->debug("~UpdateThread()");
}

void UpdateThread::start() {
	LOG->debug("start() ... spawning update thread");
	this->updateThread = boost::thread(&UpdateThread::onThreadRun, this);
}

void UpdateThread::onThreadRun() {
	LOG->info("onThreadRun() START");
	xn::DepthMetaData depthMetaData;
//	try {
		while(this->threadShouldRun) {
			this->context.WaitAnyUpdateAll();
			this->depthGenerator.GetMetaData(depthMetaData);
//			this->userManager->update();
		}
//	} catch(Exception& e) {
//		this->broadcastThreadException(e);
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
