#pragma once
#ifndef EXCEPTION_H_
#define EXCEPTION_H_

#include <ponyo/common/pncommon_inc.hpp>
#include <ponyo/common/Logging.hpp>

namespace pn {
class Exception {

public:
	/**
	 * eg: throw Exception("foobar", AT);
	 */
	Exception(const char*, const char*, int);
	virtual ~Exception();

	const char* getMessage();
	void printBacktrace();

private:
	static Log* LOG;

	const char* message;
	const char* sourceFile;
	int sourceLine;
};
}

#endif // EXCEPTION_H_
