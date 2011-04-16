#pragma once
#ifndef IMAGEWINDOW_HPP_
#define IMAGEWINDOW_HPP_

#define GL_WIN_SIZE_X XN_VGA_X_RES
#define GL_WIN_SIZE_Y XN_VGA_Y_RES

#include <ponyo/PnCommon.hpp>
#include <ponyo/openni/pnopenni_inc.hpp>
#include <ponyo/openni/includes/headers_glut.hpp>
#include <ponyo/openni/view/GlutThread.hpp>

namespace pn {


typedef unsigned int WindowAction;
static const WindowAction WINDOW_ACTION_ESCAPE = 1;

typedef void (*ImageWindowCallback) (WindowAction actionId);

class ImageWindow {
public:

	// MINOR singleton glut hack
	static ImageWindow* getInstance(ImageWindowCallback callback);
	static void destroyInstance();

	void init(xn::ImageMetaData* imageData, int xRes, int yRes, int argc, char** argv);

	bool isInitialized() const;
	bool isVisible() const;
	void setVisible(bool value);

private:
	static Log* LOG;
	static ImageWindow* instance;
	static ImageWindowCallback callback;
	static GlutThread glutThread;

	int glutWindowHandle;
	bool initialized;
	bool created;
	bool visible;

	xn::ImageMetaData* imageData;
	int xRes;
	int yRes;


	ImageWindow();
	virtual ~ImageWindow();

	static void onGlutDisplay();
	static void onGlutIdle();
	static void onGlutReshape(int width, int height);
	static void onGlutKeyboard(unsigned char key, int x, int y);
	static void onGlutVisibility(int glutState);

};
}
#endif // IMAGEWINDOW_HPP_
