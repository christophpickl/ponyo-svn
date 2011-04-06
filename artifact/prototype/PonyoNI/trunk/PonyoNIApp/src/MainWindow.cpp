#include <stdlib.h> // exit()
#include "myglut.h"


#include "MainWindow.hpp"
#include "log/LogFactory.hpp"

namespace pn {
NEW_LOG(__FILE__)

MainWindow::MainWindow() {
	LOG->debug("new MainWindow()");
}

MainWindow::~MainWindow() {
	LOG->debug("~MainWindow()");

}

void MainWindow::startInfiniteLoop(int argc, char** argv) {
	LOG->info("startInfiniteLoop()");

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(640, 480);
	/*int windowHandle = */glutCreateWindow("PonyoNI Prototype");

	glutKeyboardFunc(MainWindow::onGlutKeyboard);

	glutDisplayFunc(MainWindow::onGlutDisplay);
//	glutReshapeFunc(&MainWindow::changeSize);
//	glutIdleFunc(&MainWindow::renderScene);

	glutMainLoop();
}

/*static*/ void MainWindow::onGlutDisplay() {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear Color and Depth Buffers
	glLoadIdentity(); // reset transformations

	glutSwapBuffers();
}
/*static*/ void MainWindow::onGlutKeyboard(unsigned char key, int x, int y) {
	LOG->info("onGlutKeyboard(key, x, y)");
	switch (key) {
		case 'q':
			exit(1);
	}
}

}
