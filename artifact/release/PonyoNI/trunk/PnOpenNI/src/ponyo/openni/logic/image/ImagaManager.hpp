#pragma once
#ifndef IMAGAMANAGER_HPP_
#define IMAGAMANAGER_HPP_

#include <ponyo/openni/pnopenni_inc.hpp>

namespace pn {
class ImagaManager {
public:
	ImagaManager(xn::ImageGenerator&);
	virtual ~ImagaManager();

	void init() throw(OpenNiException);
	void unregister();

private:
	static Log* LOG;

	xn::ImageGenerator imageGenerator;
	XnCallbackHandle onDataAvailableHandle;

	static void onDataAvailable(xn::ProductionNode& node, void* cookie);

};
}
#endif // IMAGAMANAGER_HPP_
