#include <ponyo/pnopenni/image/ImageDetectorException.hpp>

namespace pn {

Log* ImageDetectorException::LOG = NEW_LOG();

ImageDetectorException::ImageDetectorException(const char* message, const char* sourceFile, int sourceLine) :
		Exception(message, sourceFile, sourceLine) {
	LOG->debug("new ImageDetectorException(..)");
}

ImageDetectorException::~ImageDetectorException() {
	LOG->debug("~ImageDetectorException()");
}

}
