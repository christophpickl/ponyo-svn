#include <ponyo/openni/view/ImageWindow.hpp>

namespace pn {

Log* ImageWindow::LOG = NEW_LOG();
ImageWindow* ImageWindow::instance = NULL;
ImageWindowCallback ImageWindow::callback = NULL;
//GlutThread ImageWindow::glutThread;

XnRGB24Pixel* g_pTexMap = NULL;
unsigned int g_nTexMapX = 0;
unsigned int g_nTexMapY = 0;

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

/*static*/ void ImageWindow::destroyInstance() {
	LOG->debug("destroyInstance()");

	if(ImageWindow::instance != NULL) {
		LOG->trace("Destroying window singleton instance.");

		if(ImageWindow::instance->visible) {
			LOG->trace("Hiding window.");
			ImageWindow::instance->setVisible(false);
		}

		if(ImageWindow::instance->created == true) {
			LOG->trace("Destroying glut window.");
			glutDestroyWindow(ImageWindow::instance->glutWindowHandle);
//			ImageWindow::glutThread.stop();
		}

		ImageWindow::instance->initialized = false;
		ImageWindow::instance->created = false;

		delete ImageWindow::instance;
	} else {
		LOG->warn("Doing nothing as no singleton instance exists.");
	}
}


void ImageWindow::init(xn::ImageMetaData* imageData, int xRes, int yRes, int argc, char** argv) {
	LOG->debug2("init(imageData, xRes, yRes, argc=%i, argv)", argc);

	this->imageData = imageData;
	this->xRes = xRes;
	this->yRes = yRes;

	LOG->trace2("Image resolution from metadata: %ix%i", this->imageData->FullXRes(), this->imageData->FullYRes());
	LOG->trace2("Image resolution from arguments: %ix%i", xRes, yRes);

	// init texture maps
	g_nTexMapX = (((unsigned short) (this->xRes - 1) / 512) + 1) * 512;
	g_nTexMapY = (((unsigned short) (this->yRes - 1) / 512) + 1) * 512;
	g_pTexMap = (XnRGB24Pixel*) malloc(g_nTexMapX * g_nTexMapY * sizeof(XnRGB24Pixel));

	// init opengl/glut
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowSize(GL_WIN_SIZE_X, GL_WIN_SIZE_Y);
	glutInitWindowPosition(100, 100);

//	MINOR if(this->imageData->PixelFormat() != XN_PIXEL_FORMAT_RGB24) {
//		throw Exception("ERROR: The device image format must be RGB24", AT);
//		return;
//	}
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
			this->glutWindowHandle = glutCreateWindow("PonyoNI Image Window");
			LOG->trace2("Created glut window with id: %i", this->glutWindowHandle);


			glutKeyboardFunc(&ImageWindow::onGlutKeyboard);
			glutIdleFunc(&ImageWindow::onGlutIdle);
			glutDisplayFunc(&ImageWindow::onGlutDisplay);
			glutReshapeFunc(&ImageWindow::onGlutReshape);
			glutVisibilityFunc(&ImageWindow::onGlutVisibility);

			// MINOR what for? ... buuut: HAS to be invoked AFTER window creation, as otherwise will kill whole process!
			glDisable(GL_DEPTH_TEST);
			glEnable(GL_TEXTURE_2D);

//			LOG->info("Spawning glut background thread ...");
//			ImageWindow::glutThread.start();
			// TODO osx glut-bug (glut main loop has to run in main thread, otherwise window will be unresponsive)
			this->created = true;
			this->visible = true;

			LOG->info("entering glutMainLoop() ... thread will never die!!!");
			glutMainLoop();
			// THIS CODE IS NEVER REACHED!

		} else {
			fprintf(stderr, "FIXME!!! reshowing window does not work // glutGetWindow() =%i\n", glutGetWindow()); // FIXME!!! reshowing window does not work
			glutShowWindow();
			glutIdleFunc(&ImageWindow::onGlutIdle);
			this->visible = true;
		}
		// only reached when not initially created, as glut main loop blocks

	} else /* (this->isVisible == false) */ {
		LOG->debug("Hiding window and nullifying idle function.");
		glutHideWindow();
		glutIdleFunc(NULL);
		this->visible = false;
	}
}


/*static*/ void ImageWindow::onGlutDisplay() {
//	LOG->debug("onGlutDisplay()");
//	LOG->trace2("Image resolution from metadata: %ix%i\n", ImageWindow::instance->imageData->FullXRes(), ImageWindow::instance->imageData->FullYRes());

	// TODO glutBitmapCharacter(void* font, 'a')

	// TODO what for???
	const XnUInt8* pImageXXXXX = ImageWindow::instance->imageData->Data();


	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	// Setup the OpenGL viewpoint
	glMatrixMode(GL_PROJECTION);
	glPushMatrix();
	glLoadIdentity();
	glOrtho(0, GL_WIN_SIZE_X, GL_WIN_SIZE_Y, 0, -1.0, 1.0);

	xnOSMemSet(g_pTexMap, 0, g_nTexMapX*g_nTexMapY*sizeof(XnRGB24Pixel));

	// draw pixels
	const XnRGB24Pixel* pImageRow = ImageWindow::instance->imageData->RGB24Data();
	XnRGB24Pixel* pTexRow = g_pTexMap + ImageWindow::instance->imageData->YOffset() * g_nTexMapX;
	for (XnUInt y = 0; y < ImageWindow::instance->imageData->YRes(); ++y) {
		const XnRGB24Pixel* pImage = pImageRow;
		XnRGB24Pixel* pTex = pTexRow + ImageWindow::instance->imageData->XOffset();
		for (XnUInt x = 0; x < ImageWindow::instance->imageData->XRes(); ++x, ++pImage, ++pTex) {
			*pTex = *pImage;
		}
		pImageRow += ImageWindow::instance->imageData->XRes();
		pTexRow += g_nTexMapX;
	}

//	printf("draw image frame to texture\n");
	glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP_SGIS, GL_TRUE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, g_nTexMapX, g_nTexMapY, 0, GL_RGB, GL_UNSIGNED_BYTE, g_pTexMap);


//	printf("display the OpenGL texture map\n");
	const int nXRes = ImageWindow::instance->imageData->FullXRes();
	const int nYRes = ImageWindow::instance->imageData->FullYRes();
//	LOG->trace2("Image resolution from metadata: %ix%i\n", nXRes, nYRes);

	glColor4f(1, 1, 1, 1);
	glBegin(GL_QUADS);
		// upper left
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		// upper right
		glTexCoord2f((float) nXRes / (float) g_nTexMapX, 0);
		glVertex2f(GL_WIN_SIZE_X, 0);
		// bottom right
		glTexCoord2f((float) nXRes / (float) g_nTexMapX, (float) nYRes / (float) g_nTexMapY);
		glVertex2f(GL_WIN_SIZE_X, GL_WIN_SIZE_Y);
		// bottom left
		glTexCoord2f(0, (float) nYRes / (float) g_nTexMapY);
		glVertex2f(0, GL_WIN_SIZE_Y);
	glEnd();

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

/*static*/ void ImageWindow::onGlutIdle() {
//	printf("ImageWindow::onGlutIdle()\n");
	glutPostRedisplay();
}

/*static*/ void ImageWindow::onGlutVisibility(int state) {
	LOG->debug2("onGlutVisibility(state=%i)", state);
}

bool ImageWindow::isInitialized() const {
	return this->initialized;
}
bool ImageWindow::isVisible() const {
	return this->visible;
}

}
