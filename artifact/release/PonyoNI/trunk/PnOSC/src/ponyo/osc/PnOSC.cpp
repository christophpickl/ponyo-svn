#include <ponyo/osc/PnOSC.hpp>

namespace pn {

Log* PnOSC::LOG = NEW_LOG();

PnOSC::PnOSC() {
	LOG->debug("new PnOSC()");
}

PnOSC::~PnOSC() {
	LOG->debug("~PnOSC()");

}

void PnOSC::startup() {
	LOG->debug("startup()");
}

}
