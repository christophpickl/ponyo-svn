#include <stdio.h>
#include "MyService.h"

int main() {
	printf("main() START\n");

	MyService* service = new MyService();
	const int number = service->add2(40);
	printf("service->add2(40) == %i\n", number);

	printf("main() END\n");
	return 0;
}
