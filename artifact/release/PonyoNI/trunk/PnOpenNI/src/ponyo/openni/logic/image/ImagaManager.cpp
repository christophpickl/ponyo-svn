#include <ponyo/openni/logic/image/ImagaManager.hpp>

namespace pn {

Log* ImagaManager::LOG = NEW_LOG();

ImagaManager* GLUT_SINGLETON_HACK;

ImagaManager::ImagaManager(xn::ImageGenerator& generator) : generator(generator), window(NULL) {
	LOG->debug("new ImagaManager(..)");
}

/*static*/ void ImagaManager::onWindowAction(WindowAction actionId) {
	LOG->info2("onWindowAction(actionId=%i)", actionId);

	switch(actionId) {
		case WINDOW_ACTION_ESCAPE:
			GLUT_SINGLETON_HACK->setWindowVisible(false);
			break;
		default:
			LOG->warn2("Unhandled window action [%i]!", actionId);
	}
}
ImagaManager::~ImagaManager() {
	LOG->debug("~ImagaManager()");
}

void ImagaManager::init() throw(OpenNiException) {
	LOG->debug("init()");

	GLUT_SINGLETON_HACK = this;

	CHECK_XN(this->generator.RegisterToNewDataAvailable(&ImagaManager::onDataAvailable, this, this->onDataAvailableHandle), "Registering to image generator failed!");
}

void ImagaManager::setWindowVisible(bool setToVisible) {
	LOG->debug2("setWindowVisible(visible=%s)", boolToString(setToVisible));

	if(this->window == NULL) { //
		LOG->debug("window is null, lazy instantiating singleton");
		this->window = ImageWindow::getInstance(&ImagaManager::onWindowAction);
	}

	if(setToVisible == true && this->window->isInitialized() == false) { // lazy initialization
		char *argv[] = { "PonyoArgvForWindow" }; // TODO proper glut args?!
		this->window->init(1, argv);
		// TODO delete[] argv ??
	}
	this->window->setVisible(setToVisible);
}

void ImagaManager::destroy() {
	LOG->debug("destroy()");

	this->generator.UnregisterFromNewDataAvailable(this->onDataAvailableHandle);

	if(this->window != NULL) {
		LOG->trace("Destroying image window singleton instance.");
		ImageWindow::destroyInstance();
	}

}

/*static*/ void ImagaManager::onDataAvailable(xn::ProductionNode& node, void* cookie) {
//	ImagaManager* tthis = static_cast<ImagaManager*>(cookie);

//    tthis->imageGenerator.GetMetaData(tthis->recentImageData);
}

}
