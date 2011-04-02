#include <stdio.h>
//#include <stdlib.h>

//#include "HelloGlutTriangle.h"
#include "KinectWindow.h"

// #if(is OSX) // TODO check for OS
//	#include <GLUT/glut.h>
//	#include <glut.h>
// #else
//	#include <GL/glut.h>
// #endif


int main(int argc, char** argv) {
	printf("glut main() START\n");

	KinectWindow* window = new KinectWindow();
	window->display(argc, argv);
	delete window;

//	HelloGlutTriangle* glut = new HelloGlutTriangle();
//	glut->display(argc, argv);
	// delete glut;

	printf("glut main() END\n");
	return 0;
}
