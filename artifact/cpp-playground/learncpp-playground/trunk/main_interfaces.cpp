#include <stdio.h>
//#include "IService.hpp"
#include "Util.hpp"
#include "MyService.hpp"

void useIt(IService* service) {
	service->sayHello();
	try {
		service->connect();
	} catch(ConnectionException& e) {
		printf("Connection exception was thrown: %s\n", e.getMessage());
		e.printBacktrace();
	}
}

int main() {
	printf("main() START\n");
	try {
		MyService* service = new MyService();

		useIt(service);
	} catch(Exception& e) {
		printf("Generic exception was thrown: %s\n", e.getMessage());
	}

	printf("main() END\n");
	return 0;
}

