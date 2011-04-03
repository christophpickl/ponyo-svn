#include <stdio.h>
#include <stdlib.h> // free()
#include <execinfo.h> // backtrace()
#include "Exception.hpp"


Exception::Exception(const char* pMessage, const char* pSourceFile, int pSourceLine) :
	message(pMessage), sourceFile(pSourceFile), sourceLine(pSourceLine) {
	printf("new Exception(message)\n");
}

Exception::~Exception() {
	printf("~Exception()\n");
}

// TODO void toString() {
	// message
//	printf("AT: %s:%i\n", AT);
	// backtrace
//}

const char* Exception::getMessage() {
	return this->message;
}

void Exception::printBacktrace() {
	void* tracesArray[STACKTRACE_MAX_SIZE];
	size_t traceSize = backtrace(tracesArray, STACKTRACE_MAX_SIZE);
	char** traceSymbols = backtrace_symbols(tracesArray, traceSize);

//	printf ("Obtained %zd stack frames.\n", traceSize);
	fprintf(stderr, "Backtrace %s#%i:\n", this->sourceFile, this->sourceLine);
	if(traceSymbols != NULL) {
		for (int i = 1/*skip this*/; i < traceSize; ++i) {
			fprintf(stderr, "\t(%d) %s\n", i, traceSymbols[i]);
		}
		free(traceSymbols);
	} else {
		fprintf(stderr, "");
	}
}
