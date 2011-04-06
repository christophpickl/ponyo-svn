#include <stdio.h>

#include "OpenNiException.hpp"

namespace pn {

OpenNiException::OpenNiException() {
	printf("new OpenNiException()");
}

OpenNiException::~OpenNiException() {
}

}
