#ifndef ISERVICE_HPP_
#define ISERVICE_HPP_

//namespace pn {

#include "ConnectionException.hpp"

class IService {

public:

	virtual void sayHello() = 0;

	virtual void connect() throw (ConnectionException) = 0;
};

//}

#endif /* ISERVICE_HPP_ */
