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


	// mxArray *T = NULL, *result = NULL;
	// double time[10] = { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 };
//	T = mxCreateDoubleMatrix(1, 10, mxREAL);
//	memcpy((void *)mxGetPr(T), (void *)time, sizeof(time));
		// engPutVariable(ep, "T", T);
	// engEvalString(ep, "D = .5.*(-9.8).*T.^2;");
	// engEvalString(ep, "plot(T,D);");

//	mxDestroyArray(result);
	engClose(engine);

	printf("Done!\n");
	return EXIT_SUCCESS;
}
