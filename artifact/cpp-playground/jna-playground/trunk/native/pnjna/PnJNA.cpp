#include "PnJNA.hpp"

foobarCallback currentCallback = 0;

extern "C" void addCallback(foobarCallback cb) {
//	cb(999);
	currentCallback = cb;
}

extern "C" int pnGetNumber() {
	if(currentCallback != 0) {
		currentCallback(6969);
		return 1111;
	}
	return 0;
}
