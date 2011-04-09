#include <stdio.h>
#include <string>
#include <ponyo/pncommon/common.hpp>
#include <ponyo/pncommon/log/LogFactory.hpp>

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
