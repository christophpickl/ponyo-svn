#include <stdio.h>
#include "MyService.hpp"
#include "DummyServiceListener.h"

using namespace pn;

int main() {
	printf("main()\n");

	IServiceListener* listener = (IServiceListener*) new DummyServiceListener();

//	Async<IServiceListener*>* async = new Async<IServiceListener*>();
//	async->add(listener);
//	delete async;

	MyService* service = new MyService();
	service->add(listener);
	service->sayHello();
	delete service;

	return 0;
}

