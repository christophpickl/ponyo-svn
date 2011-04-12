#include <stdlib.h> // free()
#include <execinfo.h> // backtrace()

#include <ponyo/common/exception/Exception.hpp>

#define STACKTRACE_MAX_SIZE 42

namespace pn {

Log* Exception::LOG = NEW_LOG();

Exception::Exception(const char* pMessage, const char* pSourceFile, int pSourceLine) :
	message(pMessage), sourceFile(pSourceFile), sourceLine(pSourceLine) {
	LOG->debug("new Exception(message)");
}

Exception::~Exception() {
	LOG->debug("~Exception()");
}

// TODO void toString() {
	// message
//	printf("AT: %s:%i\n", AT);
	// backtrace
//}

const char* Exception::getMessage() const {
	return this->message;
}

void Exception::printBacktrace() const {
	void* tracesArray[STACKTRACE_MAX_SIZE];
	size_t traceSize = backtrace(tracesArray, STACKTRACE_MAX_SIZE);
	int traceSizeInt = (int) traceSize;
	char** traceSymbols = backtrace_symbols(tracesArray, traceSize);
	fprintf(stderr, "Exception: %s\nBacktrace of %s#%i:\n", this->message, this->sourceFile, this->sourceLine);
	if(traceSymbols != NULL) {
		for (int i = 1/*skip this*/; i < traceSizeInt; ++i) {
			fprintf(stderr, "\t(%d) %s\n", i, traceSymbols[i]);
		}
		free(traceSymbols);
	} else {
		fprintf(stderr, "No symbols available :(");
	}
}


}
