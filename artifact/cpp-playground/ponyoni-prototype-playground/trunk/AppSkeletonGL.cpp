#include <stdio.h>
//#ifdef __APPLE__
//#include <OpenGL/OpenGL.h>
//#include <GLUT/glut.h>
//#else
//#include <GL/glut.h>
//#endif
#ifdef FOO
#include <gl.h>
#include <glut.h>
#else
#include <GLUT/glut.h>
#endif
#include <ponyo/pnopenni/simplified/ContextX.hpp>

#include <vector>
#include <ctime>
#include <cstdlib>

int g_glutWindowHandler;
float g_i = 0.0f;
int g_cntObjects;
std::vector<float> g_RedObjects;
std::vector<float> g_XPosObjects;
std::vector<float> g_YPosObjects;
std::vector<float> g_ZPosObjects;

int randBetween(int fromInclusive, int toInclusive) {
	return rand() % (toInclusive - fromInclusive + 1) + fromInclusive;
}

class Window {
public:
	void init(int argc, char** argv) {
		glutInit(&argc, argv);
		glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
		glutInitWindowPosition(100,100);
		glutInitWindowSize(640, 480);
		g_glutWindowHandler = glutCreateWindow("PonyoNI Prototype");

		// dbwin = glutCreateSubWindow(glutGetWindow(), 0, 0, glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT));

		glutKeyboardFunc(Window::onGlutKeyboard);
		glutDisplayFunc(Window::onGlutDisplay);
	//	glutMenuStatusFunc(MainWindow::onGlutMenuStatus);
	//	glutReshapeFunc(MainWindow::onGlutReshape);
		glutIdleFunc(Window::onGlutIdle);

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1.0f);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

//		gl.glShadeModel(GL.GL_SMOOTH);
//		gl.glEnable(GL.GL_DEPTH_TEST);
//		gl.glDepthFunc(GL.GL_LEQUAL);
//		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}
	void display() {
		glutMainLoop();
	}
private:
	static void onGlutIdle() {
		glutPostRedisplay();
	}
	static void onGlutDisplay() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear Color and Depth Buffers

		for (int i = 0; i < g_cntObjects; ++i) {
			Window::drawObject(i);
		}

		g_i += 0.05f;
		if(g_i >= 360.0f) { g_i -= 360.0f; }

		glutSwapBuffers();
//		glFlush();
	}
	static void drawObject(int i) {
		glLoadIdentity();
		glColor3f(g_RedObjects.at(i), 1.0f, 0.0f);
		glScalef(0.3f, 0.3f, 0.3f);
		glTranslatef(g_XPosObjects.at(i),
					 g_YPosObjects.at(i),
					 g_ZPosObjects.at(i));
		glRotatef(g_i, 0.0f, 0.0f, 1.0f); /*(angle, x, y, z)*/

		glBegin(GL_QUADS);
			glVertex3f(-1.0f,  1.0f, 1.0f);
			glVertex3f( 1.0f,  1.0f, 1.0f);
			glVertex3f( 1.0f, -1.0f, 1.0f);
			glVertex3f(-1.0f, -1.0f, 1.0f);
		glEnd();
	}

	static void onGlutKeyboard(unsigned char key, int x, int y) {
	#pragma unused (x, y)
		switch (key) {
			case 'q':
				printf("quit");
				glutDestroyWindow(g_glutWindowHandler);
				exit(0);
				break;
		}
	}
};

int main(int argc, char** argv) {
	srand(time(0));
	g_cntObjects = randBetween(0, 5) + 10;
	printf("g_cntObjects=%i\n", g_cntObjects);

	for (int i = 0; i < g_cntObjects; ++i) {
		g_XPosObjects.push_back(randBetween(0, 100) / 100.0f *  4.0f - 2.0f);
		g_YPosObjects.push_back(randBetween(0, 100) / 100.0f *  4.0f - 2.0f);
		g_ZPosObjects.push_back(randBetween(0, 100) / 100.0f * -3.0f - 4.0f);
		g_RedObjects.push_back(randBetween(0, 100) / 100.0f);
	}

	Window win;
	win.init(argc, argv);
	win.display();
	return 0;
}
