#include <stdio.h>
#include <stdlib.h>
#include <XnCppWrapper.h>

#define CHECK_RC(returnCode, actionDescription) \
	printf(">> %s ...\n", actionDescription); \
	if(returnCode != XN_STATUS_OK) { \
		fprintf(stderr, "actionDescription: %s\nxnGetStatusString(returnCode): %s\n", actionDescription, xnGetStatusString(returnCode)); \
	}

int main() {
	xn::Context context;
	printf("main()\n");

	CHECK_RC(context.Init(), "context.Init()");

	context.Shutdown();

	return EXIT_SUCCESS;
}
