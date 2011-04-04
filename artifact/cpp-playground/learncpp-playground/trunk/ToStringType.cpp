#include <stdio.h>
#include <sstream>
#include "ToStringType.hpp"

ToStringType::ToStringType() : x(42) {
}

ToStringType::~ToStringType() {
}

std::string ToStringType::toString() {
	std::stringstream ss;
	ss << *this;
	return ss.str();
}
