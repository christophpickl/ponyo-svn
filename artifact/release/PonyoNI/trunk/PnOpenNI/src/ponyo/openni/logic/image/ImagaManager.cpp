#include <ponyo/openni/logic/image/ImagaManager.hpp>

namespace pn {

Log* ImagaManager::LOG = NEW_LOG();

ImagaManager::ImagaManager(xn::ImageGenerator& generator) : imageGenerator(generator) {
	LOG->debug("new ImagaManager(..)");
}
ImagaManager::~ImagaManager() {
	LOG->debug("~ImagaManager()");
}

void ImagaManager::init() throw(OpenNiException) {
	LOG->debug("init()");

	CHECK_XN(this->imageGenerator.RegisterToNewDataAvailable(&ImagaManager::onDataAvailable, this, this->onDataAvailableHandle), "Registering to image generator failed!");
}

void ImagaManager::unregister(){
	LOG->debug("unregister()");

	this->imageGenerator.UnregisterFromNewDataAvailable(this->onDataAvailableHandle);
}

/*static*/ void ImagaManager::onDataAvailable(xn::ProductionNode& node, void* cookie) {
//	ImageGenerator* tthis = static_cast<ImageGenerator*>(cookie);
	printf("onImageData!!!\n");
//	printf("onImageDataAvailable(..) >> tthis->imageGenerator.WaitAndUpdateData();\n");
//    tthis->imageGenerator.WaitAndUpdateData();
//    tthis->imageGenerator.GetMetaData(tthis->recentImageData);

    // TODO lock; write MD; unlock
}

}
