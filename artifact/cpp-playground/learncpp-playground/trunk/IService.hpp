#pragma once
#ifndef ISERVICE_HPP_
#define ISERVICE_HPP_

#include "ConnectionException.hpp"
#include "Async.hpp"
#include "IServiceListener.hpp"

namespace pn {
class IService : public Async<IServiceListener*> {

public:

	virtual void sayHello() = 0;

	virtual void connect() throw (ConnectionException) = 0;
};
}

#endif /* ISERVICE_HPP_ */
