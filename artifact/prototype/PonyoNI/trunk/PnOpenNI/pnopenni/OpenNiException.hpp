#pragma once
#ifndef OPENNIEXCEPTION_H_
#define OPENNIEXCEPTION_H_

#include "common.hpp"
#include "Exception.hpp"

namespace pn {
class OpenNiException : public Exception {
public:
	OpenNiException(const char*, const char*, int);
	virtual ~OpenNiException();
private:
	static Log* LOG;
};
}

#endif // OPENNIEXCEPTION_H_
