#include <stdio.h>

#include "Log.hpp"

Log::Log(const char* pSourceFile) : sourceFile(pSourceFile) {
	printf("[LOGLOG] new Log(sourceFile=%s)\n", pSourceFile);
}

Log::~Log() {
	printf("[LOGLOG] ~Log()\n");
}

void Log::debug(const char* message) {
	printf("[DEBUG] %s -- %s\n", this->sourceFile, message);
}
