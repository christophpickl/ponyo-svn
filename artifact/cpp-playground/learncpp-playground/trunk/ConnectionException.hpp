#pragma once
#ifndef CONNECTIONEXCEPTION_H_
#define CONNECTIONEXCEPTION_H_

#include "Exception.hpp"

class ConnectionException : public Exception {

public:
	ConnectionException(const char*, const char*, int);
	virtual ~ConnectionException();

};

#endif /* CONNECTIONEXCEPTION_H_ */
