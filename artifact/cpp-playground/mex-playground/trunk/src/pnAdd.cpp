
#include <stdio.h>
#include <mex.h>
#include "pn_mex_common.h"

#define MEX_FN_SIGNATURE "[1] { int } pnAdd([2] { int, int })"

void pnError(char*);

/**
 * [1] { int } pnAdd([2] { int, int })
 */
void mexFunction(int outArgc, mxArray *outArgs[], int inArgc, const mxArray* inArgs[]) {

	dumpInArgs(84);

	if(inArgc != 2) { pnError("inArgc != 2"); }
	if(!mxIsInt32(inArgs[0])){ pnError("!mxIsInt8(inArgs[0])"); }
	if(!mxIsInt32(inArgs[1])){ pnError("!mxIsInt8(inArgs[1])"); }

    /* Check to make sure input argument is a scalar */
//    if (mxGetN(prhs[0]) != 1 || mxGetM(prhs[0]) !=1){
//      mexErrMsgTxt("Input must be a scalar handle value.\n");
//    }
	int x = mxGetScalar(inArgs[0]);
	mexPrintf("x=%i\n", x);
}

void pnError(char* message) {
	mexPrintf("Ponyo ERROR! function signatue: %s\n", MEX_FN_SIGNATURE);
	mexErrMsgTxt(message);
}
