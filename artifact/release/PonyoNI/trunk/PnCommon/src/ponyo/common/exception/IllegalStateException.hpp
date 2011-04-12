#pragma once
#ifndef ILLEGALSTATEEXCEPTION_HPP_
#define ILLEGALSTATEEXCEPTION_HPP_

#include <ponyo/common/exception/Exception.hpp>

namespace pn {

/**
 * @version 0.1
 */
class IllegalStateException : public Exception {
public:
	IllegalStateException(const char* message, const char* sourceFile, int sourceLine);
	virtual ~IllegalStateException();
private:
	static Log* LOG;
};
}

#endif // ILLEGALSTATEEXCEPTION_HPP_
