#pragma once
#ifndef EXCEPTION_H_
#define EXCEPTION_H_

#include "Util.hpp"
#define STACKTRACE_MAX_SIZE 42

class Exception {

	public:
		Exception(const char*, const char*, int);
		virtual ~Exception();

		const char* getMessage();
		void printBacktrace();

	private:
		const char* message;
		const char* sourceFile;
		int sourceLine;
};

#endif /* EXCEPTION_H_ */
