#include <stdio.h>
//#include <glut.h>
#include <GLUT/glut.h>
#include "HelloGlutTriangle.h"

float xangle = 0.0f;
HelloGlutTriangle::HelloGlutTriangle() {
	printf("new HelloGlutTriangle()\n");
}

HelloGlutTriangle::~HelloGlutTriangle() {
	printf("delete ~HelloGlutTriangle()\n");
}

void HelloGlutTriangle::display(int argc, char** argv) {
	printf("HelloGlutTriangle.display(..)\n");
	// init GLUT and create window
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH /* or: GLUT_RGB | GLUT_SINGLE */);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(320,320);
	/*int windowHandle = */glutCreateWindow("Glut Playground Window");
	// TODO make use of: int windowHandle = glutCreateWindow()

	// register callbacks
	glutDisplayFunc(&HelloGlutTriangle::renderScene);
	glutReshapeFunc(&HelloGlutTriangle::changeSize);
	glutIdleFunc(&HelloGlutTriangle::renderScene);

	glutMainLoop(); // enter GLUT event processing cycle
}

// see: http://www.lighthouse3d.com/tutorials/glut-tutorial/preparing-the-window-for-a-reshape/
/*static*/ void HelloGlutTriangle::changeSize(int w, int h) {
	printf("HelloGlutTriangle::changeSize(w=%i, h=%d)\n", w, h);

	// Prevent a divide by zero, when window is too short (you cant make a window of zero width).
	if(h == 0) { h = 1; }

	float ratio =  w * 1.0 / h;
	glMatrixMode(GL_PROJECTION); // Use the Projection Matrix
	glLoadIdentity(); // Reset Matrix
	glViewport(0, 0, w, h); // Set the viewport to be the entire window
	gluPerspective(45.0f, ratio, 0.1f, 100.0f); // Set the correct perspective.
	glMatrixMode(GL_MODELVIEW); // Get Back to the Modelview
}

/*static*/ void HelloGlutTriangle::renderScene() {
	printf("HelloGlutTriangle.renderScene()\n");

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear Color and Depth Buffers

	glLoadIdentity(); // reset transformations
	// set the camera
	gluLookAt(	0.0f, 0.0f, 10.0f,
				0.0f, 0.0f,  0.0f,
				0.0f, 1.0f,  0.0f);

	glRotatef(xangle, 0.0f, 1.0f, 0.0f);

	glBegin(GL_TRIANGLES);
		glVertex3f(-2.0f,-2.0f, 0.0f);
		glVertex3f( 2.0f, 0.0f, 0.0);
		glVertex3f( 0.0f, 2.0f, 0.0);
	glEnd();

	xangle += 0.1f;
//	HelloGlutTriangle::angle = 0.1f + HelloGlutTriangle::angle;
	glutSwapBuffers();
}
