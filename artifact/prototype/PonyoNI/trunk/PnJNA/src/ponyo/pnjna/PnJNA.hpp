#pragma once
#ifndef PNJNA_HPP_
#define PNJNA_HPP_


/*public*/ typedef void(*UserStateChangedCallback)(unsigned int userId, unsigned int userState);

/*public*/ typedef void(*JointPositionChangedCallback)(unsigned int userId, unsigned int jointId, float x, float y, float z);

/*public*/ typedef void(*UpdateThreadThrewExceptionCallback)(const char* exceptionMessage);


/*public*/ extern "C" void start(
		UserStateChangedCallback,
		JointPositionChangedCallback,
		UpdateThreadThrewExceptionCallback
);

/*public*/ extern "C" void startRecording(
		UserStateChangedCallback,
		JointPositionChangedCallback,
		UpdateThreadThrewExceptionCallback,
		const char* oniFilePath
);

/*public*/ extern "C" void destroy();

/*private*/ void _start(
		UserStateChangedCallback,
		JointPositionChangedCallback,
		UpdateThreadThrewExceptionCallback,
		const char* oniFilePath
);

#endif // PNJNA_HPP_
