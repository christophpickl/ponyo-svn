#include <stdio.h>
//#include <glut.h>
#include <GLUT/glut.h>
#include <XnCppWrapper.h>
#include "KinectWindow.h"

using namespace xn;

#define SAMPLE_XML_PATH "/niconfig/simple_config.xml"

#define GL_WIN_SIZE_X 1280
#define GL_WIN_SIZE_Y 1024

#define DISPLAY_MODE_OVERLAY	1
#define DISPLAY_MODE_DEPTH		2
#define DISPLAY_MODE_IMAGE		3
#define DEFAULT_DISPLAY_MODE	DISPLAY_MODE_DEPTH

#define MAX_DEPTH 10000

float g_pDepthHist[MAX_DEPTH];
XnRGB24Pixel* g_pTexMap = NULL;
unsigned int g_nTexMapX = 0;
unsigned int g_nTexMapY = 0;

unsigned int g_nViewState = DEFAULT_DISPLAY_MODE;

Context g_context;
DepthGenerator g_depth;
ImageGenerator g_image;
DepthMetaData g_depthMD;
ImageMetaData g_imageMD;

KinectWindow::KinectWindow() {
	printf("new KinectWindow()\n");
}

KinectWindow::~KinectWindow() {
	printf("delete ~KinectWindow()\n");
}

void KinectWindow::display(int argc, char** argv) {
	printf("KinectWindow.display(..)\n");
	XnStatus rc;

	EnumerationErrors errors;
	rc = g_context.InitFromXmlFile(SAMPLE_XML_PATH, &errors);
	if (rc == XN_STATUS_NO_NODE_PRESENT) {
		XnChar strError[1024];
		errors.ToString(strError, 1024);
		printf("ERROR: %s\n", strError);
		return;
	}
	else if (rc != XN_STATUS_OK) {
		printf("ERROR: Open failed: %s\n", xnGetStatusString(rc));
		return;
	}

	rc = g_context.FindExistingNode(XN_NODE_TYPE_DEPTH, g_depth);
	rc = g_context.FindExistingNode(XN_NODE_TYPE_IMAGE, g_image);

	g_depth.GetMetaData(g_depthMD);
	g_image.GetMetaData(g_imageMD);

	// Hybrid mode isn't supported in this sample
	if (g_imageMD.FullXRes() != g_depthMD.FullXRes() || g_imageMD.FullYRes() != g_depthMD.FullYRes()) {
		printf("ERROR: The device depth and image resolution must be equal!\n");
		return;
	}

	// RGB is the only image format supported.
	if (g_imageMD.PixelFormat() != XN_PIXEL_FORMAT_RGB24) {
		printf("ERROR: The device image format must be RGB24\n");
		return;
	}

	// Texture map init
	g_nTexMapX = (((unsigned short)(g_depthMD.FullXRes()-1) / 512) + 1) * 512;
	g_nTexMapY = (((unsigned short)(g_depthMD.FullYRes()-1) / 512) + 1) * 512;
	g_pTexMap = (XnRGB24Pixel*)malloc(g_nTexMapX * g_nTexMapY * sizeof(XnRGB24Pixel));

	// OpenGL init
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowSize(GL_WIN_SIZE_X, GL_WIN_SIZE_Y);
	glutCreateWindow ("Kinect Window");
	glutFullScreen();
	glutSetCursor(GLUT_CURSOR_NONE);

	glutKeyboardFunc(KinectWindow::glutKeyboard);
	glutDisplayFunc(KinectWindow::glutDisplay);
	glutIdleFunc(KinectWindow::glutIdle);

	glDisable(GL_DEPTH_TEST);
	glEnable(GL_TEXTURE_2D);

	// Per frame code is in glutDisplay
	glutMainLoop();
}

/*static*/ void KinectWindow::glutIdle () {
	// Display the frame
	glutPostRedisplay();
}

/*static*/ void KinectWindow::glutDisplay() {
	XnStatus rc = XN_STATUS_OK;

	// Read a new frame
	rc = g_context.WaitAnyUpdateAll();
	if (rc != XN_STATUS_OK)
	{
		printf("Read failed: %s\n", xnGetStatusString(rc));
		return;
	}

	g_depth.GetMetaData(g_depthMD);
	g_image.GetMetaData(g_imageMD);

	const XnDepthPixel* pDepth = g_depthMD.Data();
	const XnUInt8* pImage = g_imageMD.Data();

	unsigned int nImageScale = GL_WIN_SIZE_X / g_depthMD.FullXRes();

	// Copied from SimpleViewer
	// Clear the OpenGL buffers
	glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Setup the OpenGL viewpoint
	glMatrixMode(GL_PROJECTION);
	glPushMatrix();
	glLoadIdentity();
	glOrtho(0, GL_WIN_SIZE_X, GL_WIN_SIZE_Y, 0, -1.0, 1.0);

	// Calculate the accumulative histogram (the yellow display...)
	xnOSMemSet(g_pDepthHist, 0, MAX_DEPTH*sizeof(float));

	unsigned int nNumberOfPoints = 0;
	for (XnUInt y = 0; y < g_depthMD.YRes(); ++y)
	{
		for (XnUInt x = 0; x < g_depthMD.XRes(); ++x, ++pDepth)
		{
			if (*pDepth != 0)
			{
				g_pDepthHist[*pDepth]++;
				nNumberOfPoints++;
			}
		}
	}
	for (int nIndex=1; nIndex<MAX_DEPTH; nIndex++)
	{
		g_pDepthHist[nIndex] += g_pDepthHist[nIndex-1];
	}
	if (nNumberOfPoints)
	{
		for (int nIndex=1; nIndex<MAX_DEPTH; nIndex++)
		{
			g_pDepthHist[nIndex] = (unsigned int)(256 * (1.0f - (g_pDepthHist[nIndex] / nNumberOfPoints)));
		}
	}

	xnOSMemSet(g_pTexMap, 0, g_nTexMapX*g_nTexMapY*sizeof(XnRGB24Pixel));

	// check if we need to draw image frame to texture
	if (g_nViewState == DISPLAY_MODE_OVERLAY ||
		g_nViewState == DISPLAY_MODE_IMAGE)
	{
		const XnRGB24Pixel* pImageRow = g_imageMD.RGB24Data();
		XnRGB24Pixel* pTexRow = g_pTexMap + g_imageMD.YOffset() * g_nTexMapX;

		for (XnUInt y = 0; y < g_imageMD.YRes(); ++y)
		{
			const XnRGB24Pixel* pImage = pImageRow;
			XnRGB24Pixel* pTex = pTexRow + g_imageMD.XOffset();

			for (XnUInt x = 0; x < g_imageMD.XRes(); ++x, ++pImage, ++pTex)
			{
				*pTex = *pImage;
			}

			pImageRow += g_imageMD.XRes();
			pTexRow += g_nTexMapX;
		}
	}

	// check if we need to draw depth frame to texture
	if (g_nViewState == DISPLAY_MODE_OVERLAY ||
		g_nViewState == DISPLAY_MODE_DEPTH)
	{
		const XnDepthPixel* pDepthRow = g_depthMD.Data();
		XnRGB24Pixel* pTexRow = g_pTexMap + g_depthMD.YOffset() * g_nTexMapX;

		for (XnUInt y = 0; y < g_depthMD.YRes(); ++y)
		{
			const XnDepthPixel* pDepth = pDepthRow;
			XnRGB24Pixel* pTex = pTexRow + g_depthMD.XOffset();

			for (XnUInt x = 0; x < g_depthMD.XRes(); ++x, ++pDepth, ++pTex)
			{
				if (*pDepth != 0)
				{
					int nHistValue = g_pDepthHist[*pDepth];
					pTex->nRed = nHistValue;
					pTex->nGreen = nHistValue;
					pTex->nBlue = 0;
				}
			}

			pDepthRow += g_depthMD.XRes();
			pTexRow += g_nTexMapX;
		}
	}

	// Create the OpenGL texture map
	glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP_SGIS, GL_TRUE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, g_nTexMapX, g_nTexMapY, 0, GL_RGB, GL_UNSIGNED_BYTE, g_pTexMap);

	// Display the OpenGL texture map
	glColor4f(1,1,1,1);

	glBegin(GL_QUADS);

	int nXRes = g_depthMD.FullXRes();
	int nYRes = g_depthMD.FullYRes();

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

	// Swap the OpenGL display buffers
	glutSwapBuffers();
}

/*static*/ void KinectWindow::glutKeyboard(unsigned char key, int x, int y) {
	switch (key) {
		case 27:
			exit (1);
		case '1':
			g_nViewState = DISPLAY_MODE_OVERLAY;
			g_depth.GetAlternativeViewPointCap().SetViewPoint(g_image);
			break;
		case '2':
			g_nViewState = DISPLAY_MODE_DEPTH;
			g_depth.GetAlternativeViewPointCap().ResetViewPoint();
			break;
		case '3':
			g_nViewState = DISPLAY_MODE_IMAGE;
			g_depth.GetAlternativeViewPointCap().ResetViewPoint();
			break;
		case 'm':
			g_context.SetGlobalMirror(!g_context.GetGlobalMirror());
			break;
	}
}
