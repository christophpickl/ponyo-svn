#pragma once
#ifndef COMMONUTILS_HPP_
#define COMMONUTILS_HPP_

#include <ponyo/common/PnCommon.hpp>

namespace pn {
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
