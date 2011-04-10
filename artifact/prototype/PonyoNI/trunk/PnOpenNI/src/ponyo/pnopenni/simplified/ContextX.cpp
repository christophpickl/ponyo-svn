#include <ponyo/pnopenni/simplified/ContextX.hpp>

namespace pn {

Log* ContextX::LOG = NEW_LOG(__FILE__)

ContextX::ContextX() : userManager(new UserManager()), threadShouldRun(true) {
	LOG->debug("new ContextX()");

	this->mapMode.nFPS = 30;
	this->mapMode.nXRes = XN_VGA_X_RES;
	this->mapMode.nYRes = XN_VGA_Y_RES;
}

ContextX::~ContextX() {
	LOG->debug("~ContextX()");
	delete this->userManager;
}

void ContextX::init() throw(OpenNiException) {
	LOG->debug("init()");
//	initOpenniLogging();
	CHECK_RC(this->context.Init(), "context.Init()");
}

void ContextX::start() throw(OpenNiException) {
	LOG->debug("start()");

	CHECK_RC(this->depthGenerator.Create(this->context), "create depth");
	CHECK_RC(this->depthGenerator.SetMapOutputMode(this->mapMode), "set depth mode"); // mandatory, otherwise will fail

	this->userManager->init(this->context);
	this->userManager->start();

	CHECK_RC(this->depthGenerator.StartGenerating(), "depth start");

	LOG->debug("spawning update thread ...");
	this->updateThread = boost::thread(&ContextX::onThreadRun, this);
//	this->updateThread = boost::thread(ContextX::threadRun, this);
	LOG->debug("background thread running.");
}
void ContextX::addUserManagerListener(UserManagerListener* listener) {
	this->userManager->addListener(listener);
}

void ContextX::onThreadRun() {
	printf("onThreadRun() START\n");
	while(this->threadShouldRun) {
		this->context.WaitAnyUpdateAll();

		this->userManager->update();
	}
	printf("onThreadRun() END\n");
}


void ContextX::shutdown() {
	LOG->debug("shutdown()");

	LOG->debug("waiting to joint updateThread ...");
	this->threadShouldRun = false;
	this->updateThread.join();

	if(this->userManager->isRunning()) this->userManager->stop();
	if(this->depthGenerator.IsGenerating()) CHECK_RC(this->depthGenerator.StopGenerating(), "depth stop"); // TODO soft fail!
	CHECK_RC(this->context.StopGeneratingAll(), "context stop all"); // TODO soft fail!

	this->context.Shutdown();
}

}
