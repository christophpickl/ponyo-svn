#include <ponyo/common/exception/IllegalInstantiationException.hpp>

namespace pn {

Log* IllegalInstantiationException::LOG = NEW_LOG();

IllegalInstantiationException::IllegalInstantiationException(const char* sourceFile, int sourceLine) :
		Exception("Not instantiable class!", sourceFile, sourceLine) {
	LOG->debug("new IllegalInstantiationException(..)");
}

IllegalInstantiationException::~IllegalInstantiationException() {
	LOG->debug("~IllegalInstantiationException()");
}

}
