#pragma once
#ifndef PNOPENNI_INC_HPP_
#define PNOPENNI_INC_HPP_

#include <XnCppWrapper.h>
#include <XnLog.h>
#include <ponyo/common/PnCommon.hpp>
#include <ponyo/openni/OpenNiException.hpp>

#define THROW_XN_EXCEPTION(xnStatus, customErrorMessage) \
	std::string ss;                                                      \
	ss.append(customErrorMessage);                                       \
	ss.append(" (OpenNI message: ");                                     \
	ss.append(xnGetStatusString(xnStatus));                              \
	ss.append(")");                                                      \
	throw OpenNiException(ss.c_str(), AT);

#define XNTRY(xnStatus, customErrorMessage) \
		if(xnStatus != XN_STATUS_OK) { THROW_XN_EXCEPTION(xnStatus, customErrorMessage); }

#endif // PNOPENNI_INC_HPP_
