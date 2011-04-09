#pragma once
#ifndef COMMON_OPENNI_HPP_
#define COMMON_OPENNI_HPP_

#include <XnCppWrapper.h>

#include <ponyo/pncommon/common.hpp>
#include <ponyo/pnopenni/OpenNiException.hpp>

#define THROW_XN_EXCEPTION(errorMessage, returnCode) \
std::string ss;                                      \
ss.append("Custom error message: ");                 \
ss.append(errorMessage);                             \
ss.append("\nOpenNI error message: ");               \
ss.append(xnGetStatusString(returnCode));            \
std::cerr << ss << std::endl; \
throw OpenNiException(ss.c_str(), AT);

#define CHECK_RC(returnCode, errorMessage) \
if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION(errorMessage, returnCode); }

namespace pn {
	void initOpenniLogging();
}

#endif // COMMON_OPENNI_HPP_
