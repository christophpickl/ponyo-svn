#pragma once
#ifndef LOGFACTORY_HPP_
#define LOGFACTORY_HPP_

#include "log/Log.hpp"

#define AT __FILE__, __LINE__
#define NEW_LOG(sourceFile) LogFactory::getLog(sourceFile);

namespace pn {
class LogFactory {
public:
	LogFactory();
	virtual ~LogFactory();

	static Log* getLog(const char*);
};
}

#endif // LOGFACTORY_HPP_
