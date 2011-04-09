//#include <ponyo/pncommon/pninclude_opencv.h>
#include <GLUT/glut.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <ponyo/pncommon/common.hpp>
#include "MainWindow.hpp"

namespace pn {

Log* MainWindow::LOG = NEW_LOG(__FILE__)

const int MENU_LOAD_DEVICES = 10;
const int MENU_START_GENERATING = 20;
const int MENU_CALIBRATE = 30;
const int MENU_QUIT = 666;
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
	this->mainWindowHandler = glutCreateWindow("PonyoNI Prototype");

	// dbwin = glutCreateSubWindow(glutGetWindow(), 0, 0, glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT));

	glutKeyboardFunc(MainWindow::onGlutKeyboard);
	glutDisplayFunc(MainWindow::onGlutDisplay);
//	glutMenuStatusFunc(MainWindow::onGlutMenuStatus);
//	glutReshapeFunc(MainWindow::onGlutReshape);
//	glutIdleFunc(MainWindow::onGlutIdle);

	this->initMenuBar();

	glClearColor(1.0,1.0,1.0,0.0);
	glColor3f(0.0f, 0.0f, 0.0f);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
}

void MainWindow::display() {
	LOG->info("display() starting glutMainLoop()");

	glutMainLoop();
}

/*static*/ void MainWindow::onGlutDisplay() {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear Color and Depth Buffers
//	glLoadIdentity(); // reset transformations
//	glClear(GL_COLOR_BUFFER_BIT);

	glutSwapBuffers();
//	glFlush();
}

/*private*/ void MainWindow::initMenuBar() {
//	int submenid = glutCreateMenu(menu);
//	glutAddMenuEntry("Teapot", 2);
	int menid = glutCreateMenu(MainWindow::onMenuItemClicked);
	glutAddMenuEntry("(l) Load Devices", MENU_LOAD_DEVICES);
	glutAddMenuEntry("(s) Start Generating", MENU_START_GENERATING);
	glutAddMenuEntry("(c) Calibrate", MENU_CALIBRATE);
	glutAddMenuEntry("(q) Quit", MENU_QUIT);
	glutAttachMenu(GLUT_LEFT_BUTTON);
}

///*static*/ void MainWindow::onGlutMenuStatus(int status, int x, int y) {
//	LOG->debug("onGlutMenuStatus(..)");
//}

/*static*/ void MainWindow::onMenuItemClicked(int menuItemId) {
	LOG->debug("onMenuItemClicked(menuItemId)");

	switch(menuItemId) {
		case MENU_LOAD_DEVICES:     tthis->onHandleLoadDevices();     break;
		case MENU_START_GENERATING: tthis->onHandleStartGenerating(); break;
		case MENU_CALIBRATE:        tthis->onHandleCalibrate();       break;
		case MENU_QUIT:             tthis->onHandleQuit();            break;
		default: throw Exception("Unhandled menu item!", AT);
	}
	// glutPostRedisplay();
}

/*private*/ void MainWindow::onHandleLoadDevices() {
	LOG->info("onHandleLoadDevices()");
	for(int i=0, n=this->listeners.size(); i < n; i++) {
		MainWindowListener* listener = this->listeners.at(i);
		listener->onListDevices();
	}
}
/*private*/ void MainWindow::onHandleStartGenerating() {
	LOG->info("onHandleStartGenerating()");
	for(int i=0, n=this->listeners.size(); i < n; i++) {
		MainWindowListener* listener = this->listeners.at(i);
		listener->onStartGenerating();
	}
}
/*private*/ void MainWindow::onHandleCalibrate() {
	LOG->info("onHandleCaptureFrame()");
	for(int i=0, n=this->listeners.size(); i < n; i++) {
		MainWindowListener* listener = this->listeners.at(i);
		listener->onCalibrate();
	}
}
/*private*/ void MainWindow::onHandleQuit() {
	LOG->info("onHandleQuit()");
	glutDestroyWindow(this->mainWindowHandler);
	for(int i=0, n=this->listeners.size(); i < n; i++) {
		MainWindowListener* listener = this->listeners.at(i);
		listener->onQuit();
	}
}

/*static*/ void MainWindow::onGlutKeyboard(unsigned char key, int x, int y) {
	LOG->info("onGlutKeyboard(key, x, y) START");

	switch (key) {
		case 'q': tthis->onHandleQuit();            break;
		case 'l': tthis->onHandleLoadDevices();     break;
		case 's': tthis->onHandleStartGenerating(); break;
		case 'c': tthis->onHandleCalibrate();       break;
	}
	LOG->info("onGlutKeyboard(key, x, y) END");
}

}
