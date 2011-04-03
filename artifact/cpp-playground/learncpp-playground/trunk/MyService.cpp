#include <stdio.h>
#include "MyService.hpp"

namespace pn {

MyService::MyService() {
	printf("new MyService()\n");
}

MyService::~MyService() {
	printf("~MyService()\n");
}

/** implements IService */
void MyService::sayHello() {
	printf("saaaaaaaay hellooooooo!\n");

	IServiceListener* listener;
	for(int i = 0, n = this->listeners.size(); i < n; i++) {
		listener = this->listeners.at(i);
		listener->onFoo(23);
	}
}

void MyService::connect() throw (ConnectionException) {
	printf("Connecting... will throw exception\n");
	throw ConnectionException("Connection failed!", AT);
}

}
