#pragma once
#ifndef IMAGEWINDOW_HPP_
#define IMAGEWINDOW_HPP_

#define GL_WIN_SIZE_X 1280
#define GL_WIN_SIZE_Y 1024

#include <ponyo/PnCommon.hpp>
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

	void init(int argc, char** argv);

	bool isInitialized() const;
	bool isVisible() const;
	void setVisible(bool value);

private:
	static Log* LOG;
	static ImageWindow* instance;
	static float angle;
	static ImageWindowCallback callback;
	static GlutThread glutThread;
	int glutWindowHandle;

	bool initialized;
	bool created;
	bool visible;

	ImageWindow();
	virtual ~ImageWindow();

	static void onGlutDisplay();
	static void onGlutIdle();
	static void onGlutReshape(int width, int height);
	static void onGlutKeyboard(unsigned char key, int x, int y);
	static void onGlutVisibility(int state);

};
}
#endif // IMAGEWINDOW_HPP_
