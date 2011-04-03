#include <stdio.h>
//#include "IService.hpp"
#include "MyService.hpp"

void useIt(IService* service) {
	service->sayHello();
}

int main() {
	printf("main() START\n");
	MyService* service = new MyService();

	useIt(service);

	printf("main() END\n");
	return 0;
}

