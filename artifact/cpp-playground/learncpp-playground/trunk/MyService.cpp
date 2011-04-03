#include <stdio.h>
#include "MyService.hpp"

//namespace pn {

MyService::MyService() {
	printf("new MyService()\n");
}

MyService::~MyService() {
	printf("~MyService()\n");
}

/** implements IService */
void MyService::sayHello() {
	printf("saaaaaaaay hellooooooo!\n");
}

void MyService::connect() throw (ConnectionException) {
	printf("Connecting... will throw exception\n");
	throw ConnectionException("Connection failed!", AT);
}
