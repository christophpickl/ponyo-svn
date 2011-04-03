#include <stdio.h>
#include "Exception.hpp"

void dangerous() {
	printf("dangerous()\n");
	throw Exception("my exception message", AT);
}

int main() {
	printf("main() START\n");
	try {
		dangerous();
	} catch(Exception& e) {
		printf("Exception was thrown: %s\n", e.getMessage());
	}
	printf("main() END\n");
	return 0;
}

