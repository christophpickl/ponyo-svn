#include <stdio.h>
#include "app.hpp"
#include "PnCommon.hpp"

using namespace pn;

int main() {
	printf("main() version: %s\n", PNFOOEXE_VERSION);

	MyCommon* comm = new MyCommon();
	comm->printVersion();

	return 0;
}

