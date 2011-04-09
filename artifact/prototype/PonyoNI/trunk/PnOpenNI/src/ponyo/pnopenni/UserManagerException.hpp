#pragma once
#ifndef USERMANAGEREXCEPTION_HPP_
#define USERMANAGEREXCEPTION_HPP_

#include <ponyo/pnopenni/common.hpp>

namespace pn {
class UserManagerException : public Exception {
public:
	UserManagerException(const char*, const char*, int);
	virtual ~UserManagerException();
private:
	static Log* LOG;
};
}

#endif // USERMANAGEREXCEPTION_HPP_
