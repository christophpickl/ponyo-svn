#pragma once
#ifndef MYSERVICELISTENER_H_
#define MYSERVICELISTENER_H_

#include "IListener.hpp"

class MyServiceListener : public IListener {
public:
	MyServiceListener();
	virtual ~MyServiceListener();
};

#endif /* MYSERVICELISTENER_H_ */
