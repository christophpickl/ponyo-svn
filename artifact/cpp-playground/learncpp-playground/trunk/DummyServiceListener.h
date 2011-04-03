#pragma once
#ifndef DUMMYSERVICELISTENER_H_
#define DUMMYSERVICELISTENER_H_

#include "IServiceListener.hpp"

class DummyServiceListener : public IServiceListener {
public:
	DummyServiceListener();
	~DummyServiceListener();

	// implements IServiceListener
	void onFoo(int);
};

#endif /* DUMMYSERVICELISTENER_H_ */
