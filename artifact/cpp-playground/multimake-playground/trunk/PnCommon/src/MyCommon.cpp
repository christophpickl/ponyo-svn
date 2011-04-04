#include <stdio.h>

#include "PnCommonVersion.hpp"
#include "MyCommon.hpp"

namespace pn {

MyCommon::MyCommon() {
}

MyCommon::~MyCommon() {
}

void MyCommon::printVersion() {
	printf("MyCommon::printVersion() says: %s\n", PNCOMMON_VERSION);
}

}
