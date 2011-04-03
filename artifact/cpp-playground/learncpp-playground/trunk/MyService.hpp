#pragma once
#ifndef MYSERVICE_HPP_
#define MYSERVICE_HPP_

#include "IService.hpp"
#include "Async.hpp"

namespace pn {
class MyService : public IService {

public:

	MyService();
	virtual ~MyService();

	// IService implementation
	void sayHello();
	void connect() throw (ConnectionException);

private:
	static void dispatchHelloEvent();

};
}

#endif /* MYSERVICE_HPP_ */
