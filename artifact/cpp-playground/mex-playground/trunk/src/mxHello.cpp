
#include <stdio.h>
#include <mex.h>

void mexFunction(int nlhs, mxArray *plhs[], int nrhs, const mxArray *prhs[]) {

	(void) plhs; // unused parameters

//	#ifdef _WIN32
	mexPrintf("Helloooooooooo from C++ Mex!\n");
//	#else
//	printf("Hello non-win-Mex!\n");
//	#endif

}
