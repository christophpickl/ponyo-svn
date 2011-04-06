#include <string.h>
#include <iostream>
#include "common.hpp"
#include "Log.hpp"

namespace pn {

int Log::LEVEL_INFO = 40;

Log::Log(const char* pLogeeName) {
	std::string pSourceFileStr = std::string(pLogeeName);
	int strX = 30; // "/Users/phudy/_dev/cpp/PonyoNI/"
	int strN = pSourceFileStr.size() - strX - 4 /*.cpp*/;
	this->logeeName = pSourceFileStr.substr(strX, strN);
	printf("[LOGLOG] new Log(logeeName=%s)\n", this->logeeName.c_str());
}

Log::~Log() {
	printf("[LOGLOG] ~Log() ... this->logeeName=[%s]\n", this->logeeName.c_str());
}

inline void Log::writeLog(const char* message, int logLevel, const char* label /* TODO: , Exception& exception = NULL */) {
	// TODO make use of logLevel
	std::cout << label << " " << this->logeeName << " -- " << message << std::endl;
}
// TODO vararg arguments for log methods!!!
// TODO add timestamp
// TODO configurable loglevels/logsources (runtime config via configfile)
void Log::fatal(const char* message) {
	this->writeLog(message, Log::LEVEL_FATAL, LABEL_LEVEL_FATAL);
}

void Log::error(const char* message) {
	this->writeLog(message, Log::LEVEL_ERROR, LABEL_LEVEL_ERROR);
}

void Log::info(const char* message) {
	this->writeLog(message, Log::LEVEL_INFO, LABEL_LEVEL_INFO);
}

void Log::debug(const char* message) {
	this->writeLog(message, Log::LEVEL_DEBUG, LABEL_LEVEL_DEBUG);
}

void Log::trace(const char* message) {
	this->writeLog(message, Log::LEVEL_TRACE, LABEL_LEVEL_TRACE);
}

}
