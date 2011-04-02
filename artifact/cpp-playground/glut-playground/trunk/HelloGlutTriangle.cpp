#include <stdio.h>
#include <glut.h>
#include "HelloGlutTriangle.h"

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
	int windowHandle = glutCreateWindow("Glut Playground Window");
	// TODO make use of: int windowHandle = glutCreateWindow()

	// register callbacks
	glutDisplayFunc(&HelloGlutTriangle::renderScene);
	glutReshapeFunc(&HelloGlutTriangle::changeSize);

	glutMainLoop(); // enter GLUT event processing cycle
}

// see: http://www.lighthouse3d.com/tutorials/glut-tutorial/preparing-the-window-for-a-reshape/
/*static*/ void HelloGlutTriangle::changeSize(int w, int h) {
	printf("HelloGlutTriangle::changeSize(w=%i, h=%d)\n", w, h);
	// Prevent a divide by zero, when window is too short
	// (you cant make a window of zero width).
	if(h == 0)
		h = 1;
	float ratio = 1.0* w / h;

	// Use the Projection Matrix
	glMatrixMode(GL_PROJECTION);

        // Reset Matrix
	glLoadIdentity();

	// Set the viewport to be the entire window
	glViewport(0, 0, w, h);

	// Set the correct perspective.
	gluPerspective(45, ratio, 1, 1000);

	// Get Back to the Modelview
	glMatrixMode(GL_MODELVIEW);
}

/*static*/ void HelloGlutTriangle::renderScene() {
	printf("HelloGlutTriangle.renderScene()\n");
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glBegin(GL_TRIANGLES);
//	glVertex3f(-0.5,-0.5,0.0);
//	glVertex3f(0.5,0.0,0.0);
//	glVertex3f(0.0,0.5,0.0);
	glVertex3f(-2,-2,-5.0);
	glVertex3f(2,0.0,-5.0);
	glVertex3f(0.0,2,-5.0);
	glEnd();

	glutSwapBuffers();
}
