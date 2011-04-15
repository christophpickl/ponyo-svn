#pragma once
#ifndef IMAGAMANAGER_HPP_
#define IMAGAMANAGER_HPP_

#include <ponyo/openni/pnopenni_inc.hpp>
#include <ponyo/openni/view/ImageWindow.hpp>

namespace pn {
class ImagaManager {
public:
	ImagaManager(xn::ImageGenerator&, int xRes, int yRes);
	virtual ~ImagaManager();

	void init() throw(OpenNiException);
	void setWindowVisible(bool setToVisible);

	void update() throw(OpenNiException);

	void destroy();

private:
	static Log* LOG;

	xn::ImageGenerator generator;
	XnCallbackHandle onDataAvailableHandle;
	ImageWindow* window;
	xn::ImageMetaData imageData;
	int xRes;
	int yRes;

	static void onDataAvailable(xn::ProductionNode& node, void* cookie);
	static void onWindowAction(WindowAction actionId);

};
}
#endif // IMAGAMANAGER_HPP_
