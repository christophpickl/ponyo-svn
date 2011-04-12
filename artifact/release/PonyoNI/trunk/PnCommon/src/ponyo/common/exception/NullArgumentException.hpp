#pragma once
#ifndef NULLARGUMENTEXCEPTION_HPP_
#define NULLARGUMENTEXCEPTION_HPP_

#include <ponyo/common/exception/Exception.hpp>

namespace pn {

/**
 * @version 0.1
 */
class NullArgumentException : public Exception {
public:
	NullArgumentException(const char* message, const char* sourceFile, int sourceLine);
	virtual ~NullArgumentException();
private:
	static Log* LOG;
};
}

#endif // NULLARGUMENTEXCEPTION_HPP_
