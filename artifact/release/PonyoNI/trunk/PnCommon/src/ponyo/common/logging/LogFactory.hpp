#pragma once
#ifndef LOGFACTORY_HPP_
#define LOGFACTORY_HPP_

#include <ponyo/common/logging/Log.hpp>

namespace pn {

/**
 * @version 0.1
 */
class LogFactory {
public:
	LogFactory();
	virtual ~LogFactory();

	static Log* getLog(const char*);
};
}

#endif // LOGFACTORY_HPP_
