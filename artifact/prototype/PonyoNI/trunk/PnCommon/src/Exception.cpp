#include <stdio.h>

#include "Exception.hpp"

namespace pn {

Exception::Exception() {
	printf("new Exception()");
}

Exception::~Exception() {
}

}
