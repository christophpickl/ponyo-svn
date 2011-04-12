#include <ponyo/common/exception/IllegalStateException.hpp>

namespace pn {

Log* IllegalStateException::LOG = NEW_LOG();

IllegalStateException::IllegalStateException(const char* message, const char* sourceFile, int sourceLine) :
		Exception(message, sourceFile, sourceLine) {
	LOG->debug("new IllegalStateException(..)");
}

IllegalStateException::~IllegalStateException() {
	LOG->debug("~IllegalStateException()");
}

}
