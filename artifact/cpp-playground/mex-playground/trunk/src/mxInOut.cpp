
#include <stdio.h>
#include <mex.h>

void mexFunction(int nlhs, mxArray *plhs[], int nrhs, const mxArray *prhs[]) {

	mexPrintf("mxInOut ... \n");

    mexPrintf("There are %d right-hand-side argument(s).\n", nrhs);
    for(int i = 0; i < nrhs; i++)  {
    	mexPrintf("\targument %i class name: %s\n", (i+1), mxGetClassName(prhs[i]));
    }


    mexPrintf("There are %d left-hand-side argument(s).\n", nlhs);
    if(nlhs > nrhs) {
      mexErrMsgTxt("Cannot specify more outputs than inputs.\n");
    }

    for (i=0; i < nlhs; i++)  {
    	plhs[i] = mxCreateDoubleMatrix(1, 1, mxREAL);
    	*mxGetPr(plhs[i]) = (double) mxGetNumberOfElements(prhs[i]);
    }
}
