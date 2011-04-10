#include <ponyo/pnopenni/simplified/ContextX.hpp>
#include <ponyo/pnjna/PnJNA.hpp>

using namespace pn;

UserStateChangedCallback userStateChangedCallback = 0;
JointPositionChangedCallback jointPositionChangedCallback = 0;
UpdateThreadThrewExceptionCallback updateThreadThrewExceptionCallback = 0;

class UserManagerListenerAdapter : public UserManagerListener {
public:
	void onUserStateChanged(int userId, UserState userState) {
		userStateChangedCallback(userId, userState);
	}
	void onJointPositionChanged(int userId, int jointId, XnSkeletonJointPosition position) {
		jointPositionChangedCallback(userId, jointId, position.position.X, position.position.Y, position.position.Z);
	}
};
class ContextXListenerAdapter : public ContextXListener {
public:
	void onUpdateThreadException(Exception& e) {
		fprintf(stderr, "ContextXListener.onUpdateThreadException: %s\n", e.getMessage());
		e.printBacktrace();

		// TODO use global error codes instead! => would mean to add an code for each and every Exception instance
		char exceptionMessage[80];
		strcpy(exceptionMessage, e.getMessage()); // store, as would otherwise get freed from memory

		updateThreadThrewExceptionCallback(exceptionMessage);
	}
};


ContextX* context;
UserManagerListenerAdapter* userManagerListenerAdapter;
ContextXListenerAdapter* contextXListenerAdapter;

extern "C" void start(
		UserStateChangedCallback userCb,
		JointPositionChangedCallback jointCb,
		UpdateThreadThrewExceptionCallback threadExcCb) {
	_start(userCb, jointCb, threadExcCb, NULL);
}
extern "C" void startRecording(
		UserStateChangedCallback userCb,
		JointPositionChangedCallback jointCb,
		UpdateThreadThrewExceptionCallback threadExcCb,
		const char* oniFilePath) {
	_start(userCb, jointCb, threadExcCb, oniFilePath);
}

void _start(
		UserStateChangedCallback userCb,
		JointPositionChangedCallback jointCb,
		UpdateThreadThrewExceptionCallback threadExcCb,
		const char* oniFilePath) {

	userStateChangedCallback = userCb;
	jointPositionChangedCallback = jointCb;
	updateThreadThrewExceptionCallback = threadExcCb;

	userManagerListenerAdapter = new UserManagerListenerAdapter();
	contextXListenerAdapter = new ContextXListenerAdapter();

	context = new ContextX();
	context->addListener(contextXListenerAdapter);
	context->addUserManagerListener(userManagerListenerAdapter);
	context->init();

	if(oniFilePath == NULL) {
		context->start();
	} else {
		context->startRecording(oniFilePath);
	}
}

extern "C" void destroy() {
	context->shutdown();
	delete context;
	delete userManagerListenerAdapter;
	delete contextXListenerAdapter;
}
