#include <stdio.h>
#include <pthread.h>
#include <glut.h>

#include "DataSnapshot.hpp"
#include "DeviceDataGobbler.hpp"

DataSnapshot* data;
DeviceDataGobbler* gobbler;

#define STATE_IDLE 0
#define STATE_QUITTING 1
#define STATE_WAITLOCK 2
int state = STATE_IDLE;

void tearDown() {
	printf("tearDown() ... displaying window, ENDED\n");
	gobbler->stop();
	gobbler->join();
	delete gobbler;
	printf("tearDown() END\n");
	exit(0);
}

void onGlutDisplay() {
	printf("onGlutDisplay()\n");
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	if(state == STATE_IDLE) {
		glColor3f(0.0f, 1.0f, 0.0f);
	} else if(state == STATE_WAITLOCK) {
		glColor3f(1.0f, 0.0f, 0.0f);
	} else if(state == STATE_QUITTING) {
		glColor3f(0.7f, 0.7f, 0.7f);
	}
	glRectf(-0.75f,0.75f, 0.75f, -0.75f);
	glutSwapBuffers();
}

void onGlutKeyboard(unsigned char key, int x, int y) {
	printf("onGlutKeyboard(key=%c, x, y)\n", key);
	if(key == 'q') {
		printf("'q' entered.");
		state = STATE_QUITTING;
		onGlutDisplay();
		tearDown();

	} else if(key == 'g') {
		printf("onGlutKeyboard(..) ... data->lock()\n");
		state = STATE_WAITLOCK;
		onGlutDisplay();
		data->lock();
		int number = data->getNumber();
		data->unlock();
		state = STATE_IDLE;
		onGlutDisplay();
		printf("===> data.number = %i\n", number);
	}
}

void displayWindow(int argc, char **argv) {
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(320,320);
	glutCreateWindow("Threading");

	glClearColor(0.8f, 0.8f, 0.8f, 0.0f);

	glutDisplayFunc(onGlutDisplay);
	glutKeyboardFunc(onGlutKeyboard);
	glutMainLoop();
}

int main(int argc, char **argv) {
	printf("main() START\n");

	pthread_mutex_t mutex1 = PTHREAD_MUTEX_INITIALIZER;
	data = new DataSnapshot(mutex1);
	gobbler = new DeviceDataGobbler(data);
	gobbler->start();

	printf("main() ... displaying window\n");
	displayWindow(argc, argv);

	return 1; // glut requires explicit exit
}
