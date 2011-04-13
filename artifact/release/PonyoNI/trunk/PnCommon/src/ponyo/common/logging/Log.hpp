#pragma once
#ifndef LOG_HPP_
#define LOG_HPP_

#include <iostream>
#include <stdio.h>
#include <string.h>

#define LABEL_LEVEL_FATAL "[FATAL]"
#define LABEL_LEVEL_ERROR "[ERROR]"
#define LABEL_LEVEL_WARN "[WARN]"
#define LABEL_LEVEL_INFO "[INFO]"
#define LABEL_LEVEL_DEBUG "[DEBUG]"
#define LABEL_LEVEL_TRACE "[TRACE]"

namespace pn {

/**
 * @version 0.1
 */
class Log {
public:
	Log(const char*);
	virtual ~Log();

	void trace(const char* message);
	void trace2(const char* format, ...);
	void debug(const char* message);
	void debug2(const char* format, ...);
	void info(const char* message);
	void info2(const char* format, ...);
	void warn(const char* message);
	void warn2(const char* format, ...);
	void error(const char* message);
	void error2(const char* format, ...);
	void fatal(const char* message);
	void fatal2(const char* format, ...);

private:
	static int LEVEL_NONE;
	static int LEVEL_FATAL;
	static int LEVEL_ERROR;
	static int LEVEL_WARN;
	static int LEVEL_INFO;
	static int LEVEL_DEBUG;
	static int LEVEL_TRACE;
	static int LEVEL_ANY;

	std::string logeeName;
//	char* logeeName;

	void writeLog(const char* message, int logLevel, const char* label);
	void writeLog2(const char* format, va_list& args, int logLevel, const char* label);
};
}

#endif // LOG_HPP_
