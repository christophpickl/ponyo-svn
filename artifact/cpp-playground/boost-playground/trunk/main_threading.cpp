#include <stdio.h>
#include <glut.h>
#include "DataSnapshot.hpp"
#include "DeviceDataGobbler.hpp"

DataSnapshot* data;
DeviceDataGobbler* gobbler;

void tearDown() {
	printf("tearDown() ... displaying window, ENDED\n");
	gobbler->stop();
	gobbler->join();
	delete gobbler;
	printf("tearDown() END\n");
	exit(0);
}

void onGlutKeyboard(unsigned char key, int x, int y) {
	printf("onGlutKeyboard(key=%c, x, y)\n", key);
	if(key == 'q') {
		printf("'q' entered.");
		tearDown();
	} else if(key == 'g') {
		printf("onGlutKeyboard(..) ... data->lock()\n");
		data->lock();
		int number = data->getNumber();
		data->unlock();
		printf("===> data.number = %i\n", number);
	}
}

void onGlutDisplay() {
	printf("onGlutDisplay()\n");
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();
	gluLookAt(	0.0f, 0.0f, 10.0f,
				0.0f, 0.0f,  0.0f,
				0.0f, 1.0f,  0.0f);
	glBegin(GL_TRIANGLES);
		glVertex3f(-2.0f,-2.0f, 0.0f);
		glVertex3f( 2.0f, 0.0f, 0.0);
		glVertex3f( 0.0f, 2.0f, 0.0);
	glEnd();
}

void displayWindow(int argc, char **argv) {
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_SINGLE);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(320,320);
	glutCreateWindow("Threading");
	glutDisplayFunc(onGlutDisplay);
	glutKeyboardFunc(onGlutKeyboard);
	glutMainLoop();
}

int main(int argc, char **argv) {
	printf("main() START\n");

	data = new DataSnapshot();
	gobbler = new DeviceDataGobbler(data);
	gobbler->start();

	printf("main() ... displaying window\n");
	displayWindow(argc, argv);

	return 1; // glut requires explicit exit
}
