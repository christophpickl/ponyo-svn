#include <stdio.h>
#include <iostream>
//#include <stdlib.h>
#include "NiEnhanced.h"

using namespace std;

NiEnhanced* ni;

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
	shutdown(signalCode); // TODO set boolean value quit to true, and use it in while loop in main!
}

int main(void) {
	printf("main() START\n");

	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT

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
	while(true){
		ni->waitAndUpdateAll(); // TODO this one is blocking, right?
	 // Process the data
	 //		g_DepthGenerator.GetMetaData(depthMD);
	 //		g_UserGenerator.GetUserPixels(0, sceneMD);
	 //		DrawDepthMap(depthMD, sceneMD);
	 }


//	printf("Hit ENTER to QUIT\n");
//	string line;
//	getline(cin, line, '\n');

	// TODO dead code: will never be reached, as infinite loop above will be aborted by signal
	printf("main() END\n");
	shutdown(0);
}
