#pragma once
#ifndef COMMONUTILS_HPP_
#define COMMONUTILS_HPP_

#include <ponyo/common/PnCommon.hpp>

namespace pn {
class CommonUtils {
public:
	CommonUtils();
	virtual ~CommonUtils();

	static void sleep(int seconds);
private:
	static Log* LOG;

};
}

#endif // COMMONUTILS_HPP_
