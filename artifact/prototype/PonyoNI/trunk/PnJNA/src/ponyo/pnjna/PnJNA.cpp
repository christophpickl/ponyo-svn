#include <ponyo/pnopenni/simplified/ContextX.hpp>
#include <ponyo/pnjna/PnJNA.hpp>

using namespace pn;

UserStateChangedCallback userStateChangedCallback = 0;
JointPositionChangedCallback jointPositionChangedCallback = 0;

class UserManagerListenerAdapter : public UserManagerListener {
public:
	void onUserStateChanged(int userId, UserState userState) {
		userStateChangedCallback(userId, userState);
	}
	void onJointPositionChanged(int userId, XnSkeletonJoint joint, XnSkeletonJointPosition position) {
		jointPositionChangedCallback(userId, joint, position.position.X, position.position.Y, position.position.Z);
	}
};

ContextX* context;
UserManagerListenerAdapter* userManagerListenerAdapter;

extern "C" void startup(UserStateChangedCallback userCb, JointPositionChangedCallback jointCb) {
	userStateChangedCallback = userCb;
	jointPositionChangedCallback = jointCb;

	userManagerListenerAdapter = new UserManagerListenerAdapter();
	context = new ContextX();
	context->init();
	context->addUserManagerListener(userManagerListenerAdapter);

	context->start();
}

extern "C" void destroy() {
	context->shutdown();
	delete context;
	delete userManagerListenerAdapter;
}
