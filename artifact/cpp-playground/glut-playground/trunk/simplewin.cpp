#include <stdio.h>
#include <XnCppWrapper.h>
#include <GLUT/glut.h>

#define CHECK(returnCode) \
if(returnCode != XN_STATUS_OK) { fprintf(stderr, "%s\n", xnGetStatusString(returnCode)); exit(1); }

using namespace xn;

#define GL_WIN_SIZE_X XN_VGA_X_RES
#define GL_WIN_SIZE_Y XN_VGA_Y_RES

Context context;
ImageGenerator imageGenerator;
ImageMetaData imageData;
XnMapOutputMode mode;

XnRGB24Pixel* g_pTexMap = NULL;
unsigned int g_nTexMapX = 0;
unsigned int g_nTexMapY = 0;

class Win {
public:
	Win() { }
	~Win() { }
	void display(int argc, char** argv) {
		printf("display(argc, argv)\n");

		printf("imageData.FullXRes()=%i, imageData.FullYRes()=%i\n", imageData.FullXRes(), imageData.FullYRes());
		printf("imageData.XRes()=%i, imageData.YRes()=%i\n", imageData.XRes(), imageData.YRes());
		printf("imageData.XOffset()=%i, imageData.YOffset()=%i\n", imageData.XOffset(), imageData.YOffset());

		// Texture map init
		g_nTexMapX = (((unsigned short)(imageData.FullXRes()-1) / 512) + 1) * 512;
		g_nTexMapY = (((unsigned short)(imageData.FullYRes()-1) / 512) + 1) * 512;
		g_pTexMap = (XnRGB24Pixel*)malloc(g_nTexMapX * g_nTexMapY * sizeof(XnRGB24Pixel));

		glutInit(&argc, argv);
		glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
		glutInitWindowSize(GL_WIN_SIZE_X, GL_WIN_SIZE_Y);
		glutCreateWindow("Win");
//		glutFullScreen();
//		glutSetCursor(GLUT_CURSOR_NONE);

//		glutKeyboardFunc(Win::onGlutKeyboard);
		glutIdleFunc(Win::onGlutIdle);
		glutDisplayFunc(Win::onGlutDisplay);

		glDisable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);

		glutMainLoop();
	}

	static void onGlutIdle() {
		glutPostRedisplay();
	}

	static void onGlutDisplay() {
//		printf("onGlutDisplay()\n");
		const XnStatus imageUpdate = imageGenerator.WaitAndUpdateData();
		if(imageUpdate != XN_STATUS_OK) {
			fprintf(stderr, "Image read failed: %s\n", xnGetStatusString(imageUpdate));
			return;
		}

		imageGenerator.GetMetaData(imageData);
		const XnUInt64 timestamp = imageData.Timestamp();
//		printf("read imageData.timestamp: %i\n", timestamp);
		const XnUInt8* pixels = imageData.Data();


		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//		printf("setup the OpenGL viewpoint\n");
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, GL_WIN_SIZE_X, GL_WIN_SIZE_Y, 0, -1.0, 1.0);

		xnOSMemSet(g_pTexMap, 0, g_nTexMapX*g_nTexMapY*sizeof(XnRGB24Pixel));

//		printf("draw image frame to texture\n");
		const XnRGB24Pixel* pImageRow = imageData.RGB24Data();
		XnRGB24Pixel* pTexRow = g_pTexMap + imageData.YOffset() * g_nTexMapX;

		for(XnUInt y = 0; y < imageData.YRes(); ++y) {
			const XnRGB24Pixel* pImage = pImageRow;
			XnRGB24Pixel* pTex = pTexRow + imageData.XOffset();

			for(XnUInt x = 0; x < imageData.XRes(); ++x, ++pImage, ++pTex) {
				*pTex = *pImage;
			}

			pImageRow += imageData.XRes();
			pTexRow += g_nTexMapX;
		}

//		printf("Create the OpenGL texture map\n");
		glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP_SGIS, GL_TRUE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, g_nTexMapX, g_nTexMapY, 0, GL_RGB, GL_UNSIGNED_BYTE, g_pTexMap);

//		printf("Display the OpenGL texture map\n");
		glColor4f(1,1,1,1);

		glBegin(GL_QUADS);

		int nXRes = imageData.FullXRes();
		int nYRes = imageData.FullYRes();

		// upper left
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		// upper right
		glTexCoord2f((float)nXRes/(float)g_nTexMapX, 0);
		glVertex2f(GL_WIN_SIZE_X, 0);
		// bottom right
		glTexCoord2f((float)nXRes/(float)g_nTexMapX, (float)nYRes/(float)g_nTexMapY);
		glVertex2f(GL_WIN_SIZE_X, GL_WIN_SIZE_Y);
		// bottom left
		glTexCoord2f(0, (float)nYRes/(float)g_nTexMapY);
		glVertex2f(0, GL_WIN_SIZE_Y);

		glEnd();

		glutSwapBuffers();
	}
};

void initOpenNi() {
	printf("initOpenNi() START\n");
	CHECK(context.Init());

	NodeInfoList imageNodes;
	printf("enumerate ...\n");
	CHECK(context.EnumerateProductionTrees(XN_NODE_TYPE_IMAGE, NULL, imageNodes, NULL));
	NodeInfo imageNode = *imageNodes.Begin();
	printf("create node ...\n");
	CHECK(context.CreateProductionTree(imageNode));
	printf("create instance...\n");
	CHECK(imageNode.GetInstance(imageGenerator));

	mode.nFPS = 30;
	mode.nXRes = GL_WIN_SIZE_X;
	mode.nYRes = GL_WIN_SIZE_Y;
	CHECK(imageGenerator.SetMapOutputMode(mode));

	// glut window needs some initial data ;)
	printf("image - start generating\n");
	CHECK(imageGenerator.StartGenerating());
	printf("image - wait and update\n");
	CHECK(imageGenerator.WaitAndUpdateData());
	printf("image - received data\n");
	imageGenerator.GetMetaData(imageData);

	printf("initOpenNi() END\n");
}

int main(int argc, char** argv) {
	printf("main() START\n");

	initOpenNi();

	Win* win = new Win();
	win->display(argc, argv);
//	delete win; ... will never be reached

	printf("main() END\n");
	return 0;
}
