#include <stdio.h>
//#include "IService.hpp"
#include "Util.hpp"
#include "MyService.hpp"
#include "ToStringType.hpp"

void useIt(pn::IService* service) {
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

	ToStringType* Tref = new ToStringType();
	std::cout << "Ref ToStringType <<: " << *Tref << std::endl;
	std::cout << "Ref ToStringType.toString(): " << Tref->toString() << std::endl;

	ToStringType Tval;
	std::cout << "Val ToStringType <<: " << Tval << std::endl;
	std::cout << "Val ToStringType.toString(): " << Tval.toString() << std::endl;


	try {
		pn::MyService* service = new pn::MyService();

		useIt(service);
	} catch(Exception& e) {
		printf("Generic exception was thrown: %s\n", e.getMessage());
	}

	printf("main() END\n");
	return 0;
}

