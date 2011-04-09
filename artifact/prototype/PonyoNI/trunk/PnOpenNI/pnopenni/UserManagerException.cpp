#include "UserManagerException.hpp"

namespace pn {

Log* UserManagerException::LOG = NEW_LOG(__FILE__)

UserManagerException::UserManagerException(const char* message, const char* sourceFile, int sourceLine) :
		Exception(message, sourceFile, sourceLine) {
	LOG->debug("new UserManagerException(..)");
}

UserManagerException::~UserManagerException() {
	LOG->debug("~UserManagerException()");
}

}
