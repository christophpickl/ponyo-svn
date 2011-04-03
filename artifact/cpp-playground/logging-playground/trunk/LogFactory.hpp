#pragma once
#ifndef LOGFACTORY_H_
#define LOGFACTORY_H_

#include <vector>
#include "Log.hpp"

class LogFactory {
public:
	static Log* getLog(const char*);
	static void deleteLogInstances(); // pseudo destructor

private:
	LogFactory();
	virtual ~LogFactory();
};

#endif // LOGFACTORY_H_
