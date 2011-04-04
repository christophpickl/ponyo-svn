#pragma once
#ifndef TOSTRINGTYPE_HPP_
#define TOSTRINGTYPE_HPP_

#include <iostream>
#include <string.h>

class ToStringType {
public:
	ToStringType();
	virtual ~ToStringType();
	int x;
	std::string toString();

	friend std::ostream& operator<<(std::ostream& os, const ToStringType& stringee) {
		return os << "ToStringType[x=" << stringee.x << "]";
	}
};

#endif // TOSTRINGTYPE_HPP_
