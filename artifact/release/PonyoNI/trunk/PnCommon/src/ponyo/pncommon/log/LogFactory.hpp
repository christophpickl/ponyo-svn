#pragma once
#ifndef LOGFACTORY_HPP_
#define LOGFACTORY_HPP_

#include <ponyo/pncommon/log/Log.hpp>

#define NEW_LOG() LogFactory::getLog(__FILE__);

namespace pn {
class LogFactory {
public:
	LogFactory();
	virtual ~LogFactory();

	static Log* getLog(const char*);
};
}

#endif // LOGFACTORY_HPP_
