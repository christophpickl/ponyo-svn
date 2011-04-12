#include <ponyo/openni/PnContext.hpp>

namespace pn {

Log* PnContext::LOG = NEW_LOG();

PnContext::PnContext() {
	LOG->debug("new PnContext()");
}

void PnContext::startWithXml(const char* configPath) throw(OpenNiException) {
	LOG->info("startWithXml(configPath)");

	TRY(this->context.Init(), "context.Init()");
}

void PnContext::destroy() {
	LOG->info("destory()");
	this->context.Shutdown();
}

PnContext::~PnContext() {
	LOG->debug("~PnContext()");
}

}
