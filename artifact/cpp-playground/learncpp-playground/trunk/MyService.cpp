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
