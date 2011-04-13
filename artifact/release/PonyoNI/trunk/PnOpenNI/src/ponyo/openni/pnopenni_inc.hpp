#pragma once
#ifndef PNOPENNI_INC_HPP_
#define PNOPENNI_INC_HPP_

#include <XnCppWrapper.h>
#include <XnLog.h>
#include <ponyo/PnCommon.hpp>
#include <ponyo/openni/includes/check_xn.hpp>
#include <ponyo/openni/OpenNiException.hpp>
#include <ponyo/openni/UserState.hpp>

namespace pn {

typedef unsigned int UserId;


typedef void (*UserStateCallback) (UserId, UserState);

typedef void (*JointPositionCallback) (UserId, unsigned int jointId, float x, float y, float z);

// TODO typedef void (*UpdateThreadThrewExceptionCallback) (int errorCode?? const char* exceptionMessage);

}

#endif // PNOPENNI_INC_HPP_
