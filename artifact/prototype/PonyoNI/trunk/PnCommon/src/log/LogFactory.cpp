#include <stdio.h>
#include <string>
#include "common.hpp"
#include "LogFactory.hpp"

using namespace std;

namespace pn {

LogFactory::LogFactory() {
	println("new LogFactory()");
}

LogFactory::~LogFactory() {
}

/*static*/ Log* LogFactory::getLog(const char* sourceFile) {
//	printf("getLog(sourceFile=%s)\n", sourceFile);
	return new Log(sourceFile);
}

}
