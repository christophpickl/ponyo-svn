#pragma once
#ifndef USERMANAGERLISTENER_HPP_
#define USERMANAGERLISTENER_HPP_

#include <ponyo/pnopenni/UserState.hpp>

namespace pn {

class UserManagerListener {
public:

	virtual void onUserStateChanged(int userId, UserState userState) = 0;

	virtual void onJointPositionChanged(/*TODO unsigned*/int userId, int jointId, XnSkeletonJointPosition/*TODO &*/) = 0;

};
}

#endif // USERMANAGERLISTENER_HPP_
