#pragma once
#ifndef OPENNIEXCEPTION_H_
#define OPENNIEXCEPTION_H_

#include <ponyo/common/exception/Exception.hpp>

namespace pn {
class OpenNiException : public Exception {
public:
	OpenNiException(const char* message, const char* sourceFile, int sourceLine);
	virtual ~OpenNiException();
private:
	static Log* LOG;
};
}

#endif // OPENNIEXCEPTION_H_
