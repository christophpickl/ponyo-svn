#include "OpenNiManager.hpp"

namespace pn {

Log* OpenNiManager::LOG = NEW_LOG(__FILE__)

OpenNiManager::OpenNiManager(DeviceInitializer* pInitializer) : initializer(pInitializer) { //  throw (ConnectionException) {
	LOG->debug("new OpenNiManager()");
}

OpenNiManager::~OpenNiManager() {
	LOG->debug("~OpenNiManager()");
}

void OpenNiManager::init() throw (OpenNiException) {
	LOG->info("init()");

	CHECK_RC(this->context.Init(), "context.Init()");
}
void OpenNiManager::listDevices() {
	// TODO if(!initedYet) throw IllegalStateException
	LOG->info("listDevices()");
	this->initializer->fetchDevices(this->context);
}

void OpenNiManager::shutdown() {
	LOG->info("shutdown()");
	this->context.Shutdown();
}

}
