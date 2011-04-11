#pragma once
#ifndef CONTEXT_HPP_
#define CONTEXT_HPP_

#include <ponyo/common/PnCommon.hpp>

namespace pn {
class Context {
public:
	Context();
	virtual ~Context();
private:
	static Log* LOG;
};
}

#endif // CONTEXT_HPP_
