#pragma once
#ifndef ISERVICELISTENER_HPP_
#define ISERVICELISTENER_HPP_

#include "IListener.hpp"

class IServiceListener : IListener {

public:

	virtual void onFoo(int) = 0;

};

#endif /* ISERVICELISTENER_HPP_ */
