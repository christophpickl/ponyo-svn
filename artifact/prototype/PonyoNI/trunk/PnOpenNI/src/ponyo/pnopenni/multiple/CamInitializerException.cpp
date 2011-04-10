#include <ponyo/pnopenni/multiple/CamInitializerException.hpp>

namespace pn {

Log* CamInitializerException::LOG = NEW_LOG();

CamInitializerException::CamInitializerException(const char* message, const char* sourceFile, int sourceLine) :
		Exception(message, sourceFile, sourceLine) {
	LOG->debug("new CamInitializerException(..)");
}

CamInitializerException::~CamInitializerException() {
	LOG->debug("~CamInitializerException()");
}

}
