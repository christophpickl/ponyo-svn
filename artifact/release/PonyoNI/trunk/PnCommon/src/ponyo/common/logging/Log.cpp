#include <stdarg.h>
#include <ponyo/common/logging/Log.hpp>

// TODO add timestamp
// TODO configurable loglevels/logsources (runtime config via configfile)

namespace pn {

int Log::LEVEL_NONE = 999;
int Log::LEVEL_FATAL = 800;
int Log::LEVEL_ERROR = 700;
int Log::LEVEL_WARN = 600;
int Log::LEVEL_INFO = 300;
int Log::LEVEL_DEBUG = 200;
int Log::LEVEL_TRACE = 100;
int Log::LEVEL_ANY = 42;

Log::Log(const char* pLogeeName) {
	std::string pSourceFileStr = std::string(pLogeeName);

	int strX = 30; // "/Users/phudy/_dev/cpp/PonyoNI/"
	int strN = pSourceFileStr.size() - strX - 4 /*.cpp*/;
	//strcpy(this->logeeName, pSourceFileStr.substr(strX, strN).c_str());
	this->logeeName = pSourceFileStr.substr(strX, strN);

	//printf("[LOGLOG] new Log(logeeName=%s)\n", this->logeeName);
}

Log::~Log() {
//	printf("[LOGLOG] ~Log() ... this->logeeName=[%s]\n", this->logeeName.c_str());
}

inline void Log::writeLog(const char* message, int logLevel, const char* label /* TODO: , Exception& exception = NULL */) {
	// TODO check logLevel
//	std::cout << label << " " << this->logeeName << " -- " << message << std::endl;
	printf("%s %s -- %s\n", label, this->logeeName.c_str(), message);
}
inline void Log::writeLog2(const char* format, va_list& args, int logLevel, const char* label /* TODO: , Exception& exception = NULL */) {
	char buffer[256];
	vsprintf(buffer, format, args);
	printf("%s %s -- %s\n", label, this->logeeName.c_str(), buffer);
}

void Log::trace(const char* message) {
	this->writeLog(message, Log::LEVEL_TRACE, LABEL_LEVEL_TRACE);
}
void Log::trace2(const char* format, ...) {
	va_list args;
	va_start (args, format);
	this->writeLog2(format, args, Log::LEVEL_TRACE, LABEL_LEVEL_TRACE);
	va_end(args);
}
void Log::debug(const char* message) {
	this->writeLog(message, Log::LEVEL_DEBUG, LABEL_LEVEL_DEBUG);
}
void Log::debug2(const char* format, ...) {
	va_list args;
	va_start (args, format);
	this->writeLog2(format, args, Log::LEVEL_DEBUG, LABEL_LEVEL_DEBUG);
	va_end(args);
}
void Log::info(const char* message) {
	this->writeLog(message, Log::LEVEL_INFO, LABEL_LEVEL_INFO);
}
void Log::info2(const char* format, ...) {
	va_list args;
	va_start (args, format);
	this->writeLog2(format, args, Log::LEVEL_INFO, LABEL_LEVEL_INFO);
	va_end(args);
}
void Log::warn(const char* message) {
	this->writeLog(message, Log::LEVEL_WARN, LABEL_LEVEL_WARN);
}
void Log::warn2(const char* format, ...) {
	va_list args;
	va_start (args, format);
	this->writeLog2(format, args, Log::LEVEL_WARN, LABEL_LEVEL_WARN);
	va_end(args);
}
void Log::error(const char* message) {
	this->writeLog(message, Log::LEVEL_ERROR, LABEL_LEVEL_ERROR);
}
void Log::error2(const char* format, ...) {
	va_list args;
	va_start (args, format);
	this->writeLog2(format, args, Log::LEVEL_ERROR, LABEL_LEVEL_ERROR);
	va_end(args);
}
void Log::fatal(const char* message) {
	this->writeLog(message, Log::LEVEL_FATAL, LABEL_LEVEL_FATAL);
}
void Log::fatal2(const char* format, ...) {
	va_list args;
	va_start (args, format);
	this->writeLog2(format, args, Log::LEVEL_FATAL, LABEL_LEVEL_FATAL);
	va_end(args);
}

}
