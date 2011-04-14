#include <ponyo/common/exception/FileNotFoundException.hpp>

namespace pn {

Log* FileNotFoundException::LOG = NEW_LOG();

FileNotFoundException::FileNotFoundException(const char* notFoundFile, const char* sourceFile, int sourceLine) :
		Exception(notFoundFile/*TODO @exception preformat message! (a la varargs mit logging)*/, sourceFile, sourceLine) {
	LOG->debug("new FileNotFoundException(..)");
}

FileNotFoundException::~FileNotFoundException() {
	LOG->debug("~FileNotFoundException()");
}

}
