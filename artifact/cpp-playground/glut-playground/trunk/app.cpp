#include <stdio.h>
#include <stdlib.h>

// #if(is OSX) // TODO check for OS
//	#include <GLUT/glut.h>
	#include <glut.h>
// #else
//	#include <GL/glut.h>
// #endif

void renderScene(void) {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glBegin(GL_TRIANGLES);
	glVertex3f(-0.5,-0.5,0.0);
	glVertex3f(0.5,0.0,0.0);
	glVertex3f(0.0,0.5,0.0);
	glEnd();

	glutSwapBuffers();
}

int main(int argc, char** argv) {
	printf("glut main() START\n");

	// init GLUT and create window
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH/*GLUT_SINGLE | GLUT_RGB*/);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(320,320);
	glutCreateWindow("Glut Playground Window");

	// register callbacks
	glutDisplayFunc(renderScene);
//	glutReshapeFunc(ChangeSize);

	glutMainLoop(); // enter GLUT event processing cycle

	printf("glut main() END\n");
	return 0;
}
