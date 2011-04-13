#pragma once
#ifndef COMMONUTILS_HPP_
#define COMMONUTILS_HPP_

#include <ponyo/common/pncommon_inc.hpp>
#include <ponyo/common/Logging.hpp>

namespace pn {

/**
 * @version 0.1
 */
class CommonUtils {
public:
	static void sleep(int seconds);
	static void waitHitEnter(bool printDefaultPrompt = true);
private:
	static Log* LOG;

	CommonUtils();
	virtual ~CommonUtils();
};
}

#endif // COMMONUTILS_HPP_
