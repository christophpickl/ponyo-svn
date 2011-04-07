#include "myglut.h"
#include "common.hpp"
#include "MainWindow.hpp"

namespace pn {

Log* MainWindow::LOG = NEW_LOG(__FILE__)

MainWindow* tthis = NULL;

MainWindow::MainWindow() {
	LOG->debug("new MainWindow()");

	if(tthis != NULL) {
		throw Exception("Singleton invariant!", AT);
	}
	tthis = this;
}

MainWindow::~MainWindow() {
	LOG->debug("~MainWindow()");

}

void MainWindow::init(int argc, char** argv) {
	LOG->info("init(argc, argv)");

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(640, 480);
	/*int windowHandle = */glutCreateWindow("PonyoNI Prototype");

	glutKeyboardFunc(MainWindow::_onGlutKeyboard);
	glutDisplayFunc(MainWindow::onGlutDisplay);
//	glutReshapeFunc(&MainWindow::changeSize);
//	glutIdleFunc(&MainWindow::renderScene);
}

void MainWindow::display() {
	LOG->info("display()");

	glutMainLoop();
}

/*static*/ void MainWindow::onGlutDisplay() {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear Color and Depth Buffers
	glLoadIdentity(); // reset transformations

	glutSwapBuffers();
}

/*static*/ void MainWindow::_onGlutKeyboard(unsigned char key, int x, int y) {
	tthis->onGlutKeyboard(key, x, y);
}
void MainWindow::onGlutKeyboard(unsigned char key, int x, int y) {
	LOG->info("onGlutKeyboard(key, x, y)");
	switch (key) {
	case 'q':
		for(int i=0, n=this->listeners.size(); i < n; i++) {
			MainWindowListener* listener = this->listeners.at(i);
			listener->onQuit();
		}
		break;
	case 'l':
		for(int i=0, n=this->listeners.size(); i < n; i++) {
			MainWindowListener* listener = this->listeners.at(i);
			listener->onListDevices();
		}
		break;
	case 's':
		for(int i=0, n=this->listeners.size(); i < n; i++) {
			MainWindowListener* listener = this->listeners.at(i);
			listener->onStartGenerating();
		}
		break;
	case 'c':
		for(int i=0, n=this->listeners.size(); i < n; i++) {
			MainWindowListener* listener = this->listeners.at(i);
			listener->onCreateImage();
		}
		break;
	}
}

}
