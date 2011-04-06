#include <stdio.h>
#include "common.hpp"
#include "Exception.hpp"
#include "log/LogFactory.hpp"

using namespace pn;

int main() {
	println("main() START");

	new Exception();
	new LogFactory();

	println("main() END");
	return 0;
}

