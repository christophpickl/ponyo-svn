#include <stdio.h>
#include <iostream>
//#include <stdlib.h>

#include "NiEnhanced.h"
#include "MultipleKinects.h"

using namespace std;

//NiEnhanced* ni;
MultipleKinects* ni;

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

	signal(SIGINT, onSignalReceived); // hit CTRL-C keys in terminal (2)
	signal(SIGTERM, onSignalReceived); // hit stop button in eclipse CDT (15)

//	ni = new NiEnhanced();
	ni = new MultipleKinects();
//	string xmlConfigPath = "/niconfig/simple_config.xml";

	try {
	//		ni->initFromXml(xmlConfigPath);
		ni->init();
	} catch (string ex) { // TODO could create own Exception class (message + stacktrace)
		cerr << "Error while initializing: " << ex << endl;
		shutdown(1);
	}
	try {
		ni->start();
	} catch (string ex) {
		cerr << "Error while starting multiple kinects: " << ex << endl;
		shutdown(1);
	}

	printf("Connection established ...\n");

	printf("Hit CTRL-C to terminate the application.\n");
	isRunning = true;
//	while(shouldTerminate == false) {
		ni->waitForUpdate(); // this method call is blocking
	 // Process the data
	 //		g_DepthGenerator.GetMetaData(depthMD);
	 //		g_UserGenerator.GetUserPixels(0, sceneMD);
	 //		DrawDepthMap(depthMD, sceneMD);
//	 }
	isRunning = false;

//	printf("Hit ENTER to QUIT\n");
//	string line;
//	getline(cin, line, '\n');

	printf("main() END\n");
	shutdown(0);
}
