#include <stdio.h>
#include <iostream>
#include "common.hpp"
#include "Log.hpp"

Log::Log(const char* pSourceFile) : sourceFile(pSourceFile) {
	printf("[LOGLOG] new Log(sourceFile=%s)\n", pSourceFile);
}

Log::~Log() {
	printf("[LOGLOG] ~Log() ... this->sourceFile=[%s]\n", this->sourceFile);
}
// TODO rework Log as stream, to use it a la: "LOG << "foo" << bar << endl;"
// TODO vararg arguments for log methods!!!

// TODO add timestamp
// TODO merge printing to internal _log method
// TODO configurable loglevels/logsources (runtime config via configfile)
void Log::error(const char* message) {
	printf("[ERROR] %s -- %s\n", this->sourceFile, message);
}

void Log::info(const char* message) {
	std::cout << "[INFO] " << this->sourceFile << " -- " << message << std::endl;
//	printf("[INFO] %s -- %s\n", this->sourceFile, message);
}

void Log::debug(const char* message) {
	printf("[DEBUG] %s -- %s\n", this->sourceFile, message);
}

void Log::trace(const char* message) {
	printf("[TRACE] %s -- %s\n", this->sourceFile, message);
}
