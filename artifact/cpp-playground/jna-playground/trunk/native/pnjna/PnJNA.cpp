#include "PnJNA.hpp"

foobarCallback currentCallback = 0;
foobazCallback zurrentCallback = 0;

extern "C" void addCallbacks(foobarCallback cb, foobazCallback zb) {
//	cb(999);
	currentCallback = cb;
	zurrentCallback = zb;
}

extern "C" int pnGetNumber() {
	if(currentCallback != 0) {
		currentCallback(6969);
		zurrentCallback(42);
		return 1111;
	}
	return 0;
}

#define PN_RETURN_SUCCESS 0;
#define PN_RETURN_ERROR_UNKOWN 1;

extern "C" int errorSafeAdd2(int* returnCode, int operand) {
	if(operand < 0) {
		*returnCode = PN_RETURN_ERROR_UNKOWN;
		return -1;
	}

	const int result = operand + 2;
	*returnCode = PN_RETURN_SUCCESS;
	return result;
}
