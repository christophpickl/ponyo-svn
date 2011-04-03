#include <stdio.h>

#include "LogFactory.hpp"

std::vector<Log*> logInstances;

/*static*/ Log* LogFactory::getLog(const char* sourceFile) {
	Log* log = new Log(sourceFile);
	logInstances.push_back(log);
	return log;
}

/*static*/ void LogFactory::deleteLogInstances() {
	for (int i = 0, n = logInstances.size(); i < n; ++i) {
		delete logInstances.at(i);
	}
	logInstances.clear();
}

LogFactory::LogFactory() {
}

LogFactory::~LogFactory() {
}
