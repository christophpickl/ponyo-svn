#pragma once
#ifndef LOG_HPP_
#define LOG_HPP_

#define LABEL_LEVEL_FATAL "[FATAL]"
#define LABEL_LEVEL_ERROR "[ERROR]"
#define LABEL_LEVEL_WARN "[WARN]"
#define LABEL_LEVEL_INFO "[INFO]"
#define LABEL_LEVEL_DEBUG "[DEBUG]"
#define LABEL_LEVEL_TRACE "[TRACE]"

#include <iostream>
#include <string.h>

namespace pn {
class Log {
public:

	Log(const char*);
	virtual ~Log();

	void fatal(const char*);
	void error(const char*);
	void info(const char*);
	void debug(const char*);
	void trace(const char*);

private:
	static int LEVEL_FATAL;
	static int LEVEL_ERROR;
	static int LEVEL_WARN;
	static int LEVEL_INFO;
	static int LEVEL_DEBUG;
	static int LEVEL_TRACE;

	std::string logeeName;

	void writeLog(const char*, int, const char*);
};
}

#endif // LOG_HPP_
