#pragma once
#ifndef CAMINITIALIZEREXCEPTION_HPP_
#define CAMINITIALIZEREXCEPTION_HPP_

#include <ponyo/pncommon/Exception.hpp>

namespace pn {
class CamInitializerException : public Exception {
public:
	CamInitializerException(const char*, const char*, int);
	virtual ~CamInitializerException();
private:
	static Log* LOG;
};
}

#endif // CAMINITIALIZEREXCEPTION_HPP_
