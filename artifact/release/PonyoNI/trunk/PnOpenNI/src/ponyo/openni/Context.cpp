#include <ponyo/openni/Context.hpp>

namespace pn {

Log* Context::LOG = NEW_LOG();

Context::Context() {
	LOG->debug("new Context()");
}

Context::~Context() {
	LOG->debug("~Context()");
}

}
