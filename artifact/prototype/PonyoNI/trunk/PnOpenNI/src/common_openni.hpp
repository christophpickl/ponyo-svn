#pragma once
#ifndef COMMON_OPENNI_HPP_
#define COMMON_OPENNI_HPP_

#include <XnCppWrapper.h>

#include "common.hpp"
#include "OpenNiException.hpp"

#define THROW_XN_EXCEPTION(errorMessage, returnCode) \
std::string ss;                                      \
ss.append(errorMessage);                             \
ss.append(" - ");                                    \
ss.append(xnGetStatusString(returnCode));            \
std::cerr << ss << std::endl; \
throw OpenNiException(ss.c_str(), AT);

#define CHECK_RC(returnCode, errorMessage) \
if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION(errorMessage, returnCode); }

#endif // COMMON_OPENNI_HPP_
