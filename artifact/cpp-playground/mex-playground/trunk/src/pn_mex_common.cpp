
#include <iostream>
#include <sstream>
#include <string>
#include <mex.h>
#include "pn_mex_common.h"

using namespace std;

void dumpInArgs(int inArgc, const mxArray* inArgs[]) {
	stringstream ss;
	for(int i = 0; i < inArgc; i++)  {
		const char* argTypeChars = mxGetClassName(inArgs[i]);
		string argType = string(argTypeChars);
		ss << "\t" << (i+1) << ". " << argType << "\n";
	}
	string result = ss.str();
	mexPrintf("dumpInArgs() types of given (in-) arguments:\n%s\n", result.c_str());
}
