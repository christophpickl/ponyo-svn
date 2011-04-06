#include <stdio.h>
#include "Exception.hpp"

using namespace pn;

int main() {
	printf("main() START\n");

	new Exception();

	printf("main() END\n");
	return 0;
}

