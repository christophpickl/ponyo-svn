#ifndef MYSERVICE_HPP_
#define MYSERVICE_HPP_

#include "IService.hpp"

//namespace pn {

class MyService : public IService {

public:

	MyService();
	virtual ~MyService();

	// IService implementation
	void sayHello();
};

//}

#endif /* MYSERVICE_HPP_ */
