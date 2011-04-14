#pragma once
#ifndef ILLEGALINSTANTIATIONEXCEPTION_HPP_
#define ILLEGALINSTANTIATIONEXCEPTION_HPP_

#include <ponyo/common/exception/Exception.hpp>

namespace pn {

/**
 * @version 0.1
 */
class IllegalInstantiationException : public Exception {
public:
	IllegalInstantiationException(const char* sourceFile, int sourceLine);
	virtual ~IllegalInstantiationException();
private:
	static Log* LOG;
};
}

#endif // ILLEGALINSTANTIATIONEXCEPTION_HPP_
