#include <ponyo/openni/view/ImageWindow.hpp>

namespace pn {

Log* ImageWindow::LOG = NEW_LOG();
ImageWindow* ImageWindow::instance = NULL;
float ImageWindow::angle = 0.0f;
ImageWindowCallback ImageWindow::callback = NULL;

/*private*/ ImageWindow::ImageWindow() :
		initialized(false),
		visible(false)
{
	LOG->debug("new ImageWindow()");
}

/*private*/ ImageWindow::~ImageWindow() {
	LOG->debug("~ImageWindow()");
}

/*static*/ ImageWindow* ImageWindow::getInstance(ImageWindowCallback callback) {
	LOG->debug("getInstance()");


	if(ImageWindow::instance == NULL) {
		ImageWindow::callback = callback;
		ImageWindow::instance = new ImageWindow();
	} else {
		LOG->warn("Returning shared instance!");
	}
	return ImageWindow::instance;
}

/*static*/ void ImageWindow::destroy() {
	LOG->debug("destroy()");

	if(ImageWindow::instance != NULL) {
		LOG->trace("Destroying window singleton instance.");

		if(ImageWindow::instance->visible) {
			ImageWindow::instance->setVisible(false);
		}

		if(ImageWindow::instance->created == true) {
			glutDestroyWindow(ImageWindow::instance->glutWindowHandle);
		}

		ImageWindow::instance->initialized = false;
		ImageWindow::instance->created = false;

		delete ImageWindow::instance;
	} else {
		LOG->warn("Doing nothing as no singleton instance exists.");
	}
}

void ImageWindow::init(int argc, char** argv) {
	LOG->debug2("init(argc=%i, argv)", argc);

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowSize(GL_WIN_SIZE_X, GL_WIN_SIZE_Y);
	glutInitWindowPosition(100, 100);

//	TODO if(g_imageMD.PixelFormat() != XN_PIXEL_FORMAT_RGB24) {
//		printf("ERROR: The device image format must be RGB24\n");
//		return;
//	}

	glutKeyboardFunc(&ImageWindow::onGlutKeyboard);
//	glutIdleFunc(&ImageWindow::onGlutIdle); NO, as will be registered later on in setVisible(bool)
	glutDisplayFunc(&ImageWindow::onGlutDisplay);
	glutReshapeFunc(&ImageWindow::onGlutReshape);

	this->initialized = true;
}


void ImageWindow::setVisible(bool setToVisible) {
	LOG->debug2("setVisible(setToVisible=%i)", setToVisible);

	if(this->initialized == false) {
		LOG->error("Not going to display window as it was not yet initialized!");
		return;
	}

	if(setToVisible == true) {

		if(this->visible == true) {
			LOG->warn("Window already visible; ignoring.");
			return;
		}

		if(this->created == false) {

//			printf("Entering glut main loop ...\n");
//			glutMainLoop();

			this->glutWindowHandle = glutCreateWindow("PonyoNI Image Window");
			this->created = true;
		}
		glutIdleFunc(&ImageWindow::onGlutIdle);
		glutShowWindow();
		this->visible = true;

	} else /* (this->isVisible == false) */ {
		glutIdleFunc(NULL);
		glutHideWindow();
		this->visible = false;
	}
}

bool ImageWindow::isInitialized() const {
	return this->initialized;
}
bool ImageWindow::isVisible() const {
	return this->visible;
}

/*static*/ void ImageWindow::onGlutIdle() {
	glutPostRedisplay();
}


/*static*/ void ImageWindow::onGlutDisplay() {
//	LOG->debug("onGlutDisplay()");

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear Color and Depth Buffers

	glLoadIdentity(); // reset transformations
	// set the camera
	gluLookAt(	0.0f, 0.0f, 10.0f,
				0.0f, 0.0f,  0.0f,
				0.0f, 1.0f,  0.0f);

	glRotatef(ImageWindow::angle, 0.0f, 1.0f, 0.0f);

	glBegin(GL_TRIANGLES);
		glVertex3f(-2.0f,-2.0f, 0.0f);
		glVertex3f( 2.0f, 0.0f, 0.0);
		glVertex3f( 0.0f, 2.0f, 0.0);
	glEnd();

	ImageWindow::angle += 0.1f;
//	HelloGlutTriangle::angle = 0.1f + HelloGlutTriangle::angle;
	glutSwapBuffers();
}


// see: http://www.lighthouse3d.com/tutorials/glut-tutorial/preparing-the-window-for-a-reshape/
/*static*/ void ImageWindow::onGlutReshape(int width, int height) {
	LOG->debug2("onGlutReshape(width=%i, height=%i)", width, height);

	// Prevent a divide by zero, when window is too short (you cant make a window of zero width).
	if(height == 0) { height = 1; }

	float ratio =  width * 1.0 / height;
	glMatrixMode(GL_PROJECTION); // Use the Projection Matrix
	glLoadIdentity(); // Reset Matrix
	glViewport(0, 0, width, height); // Set the viewport to be the entire window
	gluPerspective(45.0f, ratio, 0.1f, 100.0f); // Set the correct perspective.
	glMatrixMode(GL_MODELVIEW); // Get Back to the Modelview
}

/*static*/ void ImageWindow::onGlutKeyboard(unsigned char key, int x, int y) {
	LOG->debug2("onGlutKeyboard(key=%i (%c), x=%i, y=%i)", key, key, x, y);
	switch(key) {
		case 27: // escape pressed
			ImageWindow::callback(WINDOW_ACTION_ESCAPE);
			break;
	}
}
}
