#pragma once
#ifndef UTILS_HPP_
#define UTILS_HPP_

#include <ponyo/common/PnCommon.hpp>

namespace pn {
class Utils {
public:
	Utils();
	virtual ~Utils();

	static void sleep(int seconds);
private:
	static Log* LOG;

};
}

#endif // UTILS_HPP_
