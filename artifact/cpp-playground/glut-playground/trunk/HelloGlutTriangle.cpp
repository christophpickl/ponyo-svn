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
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH/*GLUT_SINGLE | GLUT_RGB*/);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(320,320);
	glutCreateWindow("Glut Playground Window");

	// register callbacks
	glutDisplayFunc(&HelloGlutTriangle::renderScene);
//	glutReshapeFunc(ChangeSize);

	glutMainLoop(); // enter GLUT event processing cycle
}

/*static*/ void HelloGlutTriangle::renderScene() {
	printf("HelloGlutTriangle.renderScene()\n");
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glBegin(GL_TRIANGLES);
	glVertex3f(-0.5,-0.5,0.0);
	glVertex3f(0.5,0.0,0.0);
	glVertex3f(0.0,0.5,0.0);
	glEnd();

	glutSwapBuffers();
}
