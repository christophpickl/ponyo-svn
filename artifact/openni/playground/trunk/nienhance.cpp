#include <stdio.h>
#include <iostream>
//#include <stdlib.h>
#include "NiEnhanced.h"

using namespace std;

NiEnhanced* ni;
bool isRunning = false;
bool shouldTerminate = false;

void shutdown(int returnCode) {
	printf("shutdown(returnCode=%d)\n", returnCode);
	if(ni != NULL) {
		ni->close();
		delete ni;
		ni = NULL;
	}
	exit(returnCode);
}

void onSignalReceived(int signalCode) {
	printf("onSignalReceived(signalCode=%d)\n", signalCode);
	if(isRunning == true) {
		shouldTerminate = true;
	} else {
		shutdown(signalCode); // force quit while being in setup code
	}
}

int main(void) {
	printf("main() START\n");

	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)

	ni = new NiEnhanced();
	string xmlConfigPath = "/openni/niconfig.xml";
	try {
		ni->initFromXml(xmlConfigPath);
	} catch (string ex) { // TODO could create own Exception class (message + stacktrace)
		cerr << "ERROR: " << ex << endl;
		shutdown(1);
	}

	printf("Connection established ...\n");

	printf("Hit CTRL-C to terminate the application.\n");
	isRunning = true;
	while(shouldTerminate == false) {
		ni->waitForUpdate(); // this method call is blocking
	 // Process the data
	 //		g_DepthGenerator.GetMetaData(depthMD);
	 //		g_UserGenerator.GetUserPixels(0, sceneMD);
	 //		DrawDepthMap(depthMD, sceneMD);
	 }

//	printf("Hit ENTER to QUIT\n");
//	string line;
//	getline(cin, line, '\n');

	printf("main() END\n");
	shutdown(0);
}
