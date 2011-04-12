#pragma once
#ifndef SENDERCALLBACKING_HPP_
#define SENDERCALLBACKING_HPP_

#include <stdio.h>

template <class CallbackType>
class SenderCallbacking {
public:
	typedef void (CallbackType::*tFunction)();
//	void registerListener(CallbackType * callbackInstance,  tFunction pFunctionPointer);
	SenderCallbacking(CallbackType * callbackInstance,  tFunction pFunctionPointer) {
		printf("Sender.registerListener()\n");

		this->callbackInstance = callbackInstance;
		this->pFunction = pFunctionPointer;
	}
	void trigger() {
		printf("SenderCallbacking.trigger()\n");
		(callbackInstance->*pFunction)();
	}

private:
	CallbackType* callbackInstance;
	tFunction pFunction;
};


#endif // SENDERCALLBACKING_HPP_
