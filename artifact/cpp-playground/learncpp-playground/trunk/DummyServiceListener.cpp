#include <stdio.h>
#include "DummyServiceListener.h"

DummyServiceListener::DummyServiceListener() {
}

DummyServiceListener::~DummyServiceListener() {
}

void DummyServiceListener::onFoo(int x) {
	printf("DummyServiceListener::onFoo(x=%i)\n", x);
}
