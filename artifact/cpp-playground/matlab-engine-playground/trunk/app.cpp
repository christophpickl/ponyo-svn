
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "engine.h"

// http://www.mathworks.com/help/techdoc/matlab_external/f29148.html
// Engine programs are standalone C/C++ or Fortran programs that communicate with a separate MATLAB process via pipes

int main() {

	Engine* engine;
	printf("Opening matlab engine...\n");
	if (!(engine = engOpen("\0"))) {
		fprintf(stderr, "Could not start MATLAB engine!\n");
		return EXIT_FAILURE;
	}

//	mxDestroyArray(result);
	engClose(engine);

	printf("Done!\n");
	return EXIT_SUCCESS;
}
