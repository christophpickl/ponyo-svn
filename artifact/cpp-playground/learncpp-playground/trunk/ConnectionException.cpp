#include <stdio.h>
#include "ConnectionException.hpp"

ConnectionException::ConnectionException(const char* message, const char* sourceFile, int sourceLine) :
	Exception(message, sourceFile, sourceLine) {
	printf("new ConnectionException(message)\n");
}

ConnectionException::~ConnectionException() {
	printf("~ConnectionException()\n");
}
