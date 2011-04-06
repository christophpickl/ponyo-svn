#include <stdio.h>
#include "common.hpp"
#include "LogFactory.hpp"

namespace pn {

LogFactory::LogFactory() {
	println("new LogFactory()");
}

LogFactory::~LogFactory() {
}

/*static*/ Log* LogFactory::getLog(const char* sourceFile) {
	return new Log(sourceFile);
}

}
