#include <ponyo/jna/PnJNA.hpp>

using namespace pn;

const Log* LOG = NEW_LOG();

extern "C" void startWithXml(const char* configPath) {
	LOG->info("startWithXml(configPath)");
}

extern "C" void destroy() {
	LOG->info("destroy()");
}

