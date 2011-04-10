#pragma once
#ifndef CONTEXTXLISTENER_HPP_
#define CONTEXTXLISTENER_HPP_

#include <ponyo/pncommon/Exception.hpp>

namespace pn {
class ContextXListener {
public:
	virtual void onUpdateThreadException(Exception&) = 0;
};
}
#endif // CONTEXTXLISTENER_HPP_
