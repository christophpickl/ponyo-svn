#include "OpenNiException.hpp"

namespace pn {

Log* OpenNiException::LOG = NEW_LOG(__FILE__)

OpenNiException::OpenNiException(const char* message, const char* sourceFile, int sourceLine) :
		Exception(message, sourceFile, sourceLine) {
	LOG->debug("new OpenNiException(..)");
}

OpenNiException::~OpenNiException() {
	LOG->debug("~OpenNiException()");
}

}
