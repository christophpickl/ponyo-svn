
#include <string.h> // memcpy()
#include <stdio.h>
#include <mex.h>
#include "pn_mex_common.h"

#define MEX_FN_SIGNATURE "[1] { int } pnAdd([2] { int, int })"


void pnError(char* message, int inArgc, const mxArray* inArgs[]) {
	mexPrintf("Ponyo ERROR! function signatue: %s\n", MEX_FN_SIGNATURE);
	dumpInArgs(inArgc, inArgs);
	mexErrMsgTxt(message);
}

/**
 * [1] { int } pnAdd([2] { int, int })
 */
void mexFunction(int outArgc, mxArray *outArgs[], int inArgc, const mxArray* inArgs[]) {

//	if(outArgc != 1) { pnError("outArgc != 1", inArgc, inArgs); }
	if(inArgc != 2) { pnError("inArgc != 2", inArgc, inArgs); }
	if(!mxIsInt8(inArgs[0])){ pnError("!mxIsInt8(inArgs[0])", inArgc, inArgs); }
	if(!mxIsInt8(inArgs[1])){ pnError("!mxIsInt8(inArgs[1])", inArgc, inArgs); }

    /* Check to make sure input argument is a scalar */
//    if (mxGetN(prhs[0]) != 1 || mxGetM(prhs[0]) !=1){
//      mexErrMsgTxt("Input must be a scalar handle value.\n");
//    }
	int x = mxGetScalar(inArgs[0]);
	int y = mxGetScalar(inArgs[1]);
	// inMatrix = mxGetPr(prhs[1]);
	double result[] = { x + y };
//	mexPrintf("x=%i, y=%i\n", x, y);

	outArgs[0] = mxCreateDoubleMatrix(1, 1, mxREAL);
	double* myOutArg = mxGetPr(outArgs[0]);
	memcpy(myOutArg, result, sizeof(double));
	myOutArg[0] = result[0];

//	double *output;
//	double data[] = { 1.0, 2.1, 3.0 };
//	outArgs[0] = mxCreateDoubleMatrix(1, 3, mxREAL);
//	output = mxGetPr(outArgs[0]);
//	memcpy(output, data, 3*sizeof(double));
//	 for(int j = 0; j < 3; j++) {
//	   output[j] = data[j];
//	}

	// or one could modify in input arguments :-/ ... vin1 = (double *) mxGetPr(prhs[0]);
//	mxArray* myData = mxCreateDoubleMatrix(1, 1, mxREAL);
//	*mxGetPr(myData) = 0.42f;
//	mxSetFieldByNumber(plhs[0], 0, phone_field, field_value);
//	mxSetField(outArgs[0], 0, "myDataVar", myData);

	// mxCreateNumericArray(1, sizeof(int), mxU)
	/*
	plhs[0] = mxCreateStructMatrix(1, 1, 11, (const char**)field_name);
	tmpContext = mxCreateNumericMatrix(1, sizeof(Context*), mxUINT8_CLASS, mxREAL);
	memcpy(mxGetPr(tmpContext), (void*)&context, sizeof(Context*));
	mxSetField(plhs[0], 0, "ni_context_obj", tmpContext);
	*/
}
