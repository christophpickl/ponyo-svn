#pragma once
#ifndef EXCEPTION_H_
#define EXCEPTION_H_

#include <ponyo/common/pncommon_inc.hpp>
#include <ponyo/common/Logging.hpp>

namespace pn {

/**
 * @version 0.1
 */
class Exception {

public:
	/**
	 * eg: throw Exception("foobar", AT);
	 */
	Exception(const char*, const char*, int /*TODO add cause*/);
	virtual ~Exception();

	const char* getMessage() const;
	void printBacktrace() const;

private:
	static Log* LOG;

	const char* message;
	const char* sourceFile;
	int sourceLine;
};
}

#endif // EXCEPTION_H_
