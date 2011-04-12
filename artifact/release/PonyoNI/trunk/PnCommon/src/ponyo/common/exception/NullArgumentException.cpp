#include <ponyo/common/exception/NullArgumentException.hpp>

namespace pn {

Log* NullArgumentException::LOG = NEW_LOG();

NullArgumentException::NullArgumentException(const char* message, const char* sourceFile, int sourceLine) :
		Exception(message, sourceFile, sourceLine) {
	LOG->debug("new NullArgumentException(..)");
}

NullArgumentException::~NullArgumentException() {
	LOG->debug("~NullArgumentException()");
}

}
