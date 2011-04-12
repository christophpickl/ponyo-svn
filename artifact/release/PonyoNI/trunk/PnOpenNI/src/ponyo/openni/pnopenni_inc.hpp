#pragma once
#ifndef PNOPENNI_INC_HPP_
#define PNOPENNI_INC_HPP_

#include <XnCppWrapper.h>
#include <XnLog.h>
#include <ponyo/common/PnCommon.hpp>
#include <ponyo/openni/OpenNiException.hpp>
#include <ponyo/openni/UserState.hpp>

#define THROW_XN_EXCEPTION(xnStatus, customErrorMessage) \
	std::string ss;                                                      \
	ss.append(customErrorMessage);                                       \
	ss.append(" (OpenNI message: ");                                     \
	ss.append(xnGetStatusString(xnStatus));                              \
	ss.append(")");                                                      \
	throw OpenNiException(ss.c_str(), AT);

#define CHECK_XN(xnStatus, customErrorMessage) \
		if(xnStatus != XN_STATUS_OK) { THROW_XN_EXCEPTION(xnStatus, customErrorMessage); }

namespace pn {

typedef unsigned int UserId;

// TODO typedef void (*UpdateThreadThrewExceptionCallback) (int errorCode?? const char* exceptionMessage);

}

#endif // PNOPENNI_INC_HPP_
