#pragma once
#ifndef IMAGEDETECTOREXCEPTION_HPP_
#define IMAGEDETECTOREXCEPTION_HPP_

#include "common.hpp"
#include "Exception.hpp"

namespace pn {
class ImageDetectorException : public Exception {
public:
	ImageDetectorException(const char*, const char*, int);
	virtual ~ImageDetectorException();
private:
	static Log* LOG;
};
}

#endif // IMAGEDETECTOREXCEPTION_HPP_
