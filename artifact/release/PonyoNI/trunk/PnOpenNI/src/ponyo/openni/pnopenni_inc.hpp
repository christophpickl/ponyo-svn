#pragma once
#ifndef PNOPENNI_INC_HPP_
#define PNOPENNI_INC_HPP_

// only include things which will not result in cyclic dependencies (e.g. only low level stuff with no other includes to this module)
#include <XnCppWrapper.h>
#include <ponyo/PnCommon.hpp>
#include <ponyo/openni/includes/check_xn.hpp>
#include <ponyo/openni/OpenNiException.hpp>
#include <ponyo/openni/logic/user/UserState.hpp>

#define PN_CONFIDENCE_LIMIT 0.5


namespace pn {

typedef int UserId;

typedef void (*UserStateCallback) (UserId id, UserState state);

typedef void (*JointPositionCallback) (UserId id, unsigned int jointId, float x, float y, float z);

typedef void (*AsyncExceptionCallback) (/*TODO int errorCode??*/ const char* message, Exception& exception);

}

#endif // PNOPENNI_INC_HPP_
