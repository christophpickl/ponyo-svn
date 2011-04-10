#pragma once
#ifndef COMMON_OPENNI_HPP_
#define COMMON_OPENNI_HPP_

#include <XnCppWrapper.h>

#include <ponyo/pncommon/common.hpp>
#include <ponyo/pnopenni/OpenNiException.hpp>

// i hate c++ enums, and XnTypes does not define an artificial last enum to check against :-|
#define PN_SKEL_START 1
#define PN_SKEL_END 24

#define THROW_XN_EXCEPTION(errorMessage, returnCode) \
std::string ss;                                      \
ss.append("OpenNI Exception: ");                     \
ss.append(errorMessage);                             \
ss.append(" (Detail message: ");                     \
ss.append(xnGetStatusString(returnCode));            \
ss.append(")");                                      \
throw OpenNiException(ss.c_str(), AT);

#define CHECK_RC(returnCode, errorMessage) \
if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION(errorMessage, returnCode); }

namespace pn {
	void initOpenniLogging();
}

#endif // COMMON_OPENNI_HPP_
