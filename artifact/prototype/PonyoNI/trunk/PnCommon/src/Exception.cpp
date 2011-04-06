#include <stdio.h>
#include "common.hpp"
#include "Exception.hpp"

namespace pn {

Exception::Exception() {
	println("new Exception()");
}

Exception::~Exception() {
}

}
