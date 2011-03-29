#include <stdio.h>
//#include <stdlib.h>
#include "NiEnhanced.h"

//#include <XnOpenNI.h>
//#include <XnCodecIDs.h>
#include <XnCppWrapper.h>

xn::Context g_Context;

int main(void) {
	printf("main() START\n");

	NiEnhanced* ni = new NiEnhanced();


	delete ni;

	printf("main() END\n");
	return 0;
}
