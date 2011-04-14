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

typedef unsigned int UserId;

	// depthGeneratorEnabled
	// userGeneratorEnabled --> if userGen enabled, depthGen HAS to be enabled as well (validate config!)
//	bool imageGeneratorEnabled;
	// map output mode
	// mirror mode
	// smoothing

typedef void (*UserStateCallback) (UserId, UserState);

typedef void (*JointPositionCallback) (UserId, unsigned int jointId, float x, float y, float z);

// TODO typedef void (*UpdateThreadThrewExceptionCallback) (int errorCode?? const char* exceptionMessage);

}

#endif // PNOPENNI_INC_HPP_
