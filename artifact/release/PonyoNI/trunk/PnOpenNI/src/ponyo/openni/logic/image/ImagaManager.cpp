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

	printf("A\n");
	if(this->window == NULL) { // lazy instantiation
	printf("B -- window is null, creating!\n");
		this->window = ImageWindow::getInstance(&ImagaManager::onWindowAction);
	}
	std::cout << "window: " << this->window << std::endl;

	printf("C\n");
	if(setToVisible == true && this->window->isInitialized() == false) { // lazy initialization
	printf("D\n");
		char *argv[]={ "PonyoArgvForWindow" };
	printf("E\n");
		this->window->init(1, argv);
	printf("F\n");
		// TODO delete[] argv ??
	}

	printf("G\n");
	this->window->setVisible(setToVisible);
	printf("H\n");
}

void ImagaManager::destroy() {
	LOG->debug("destroy()");

	this->generator.UnregisterFromNewDataAvailable(this->onDataAvailableHandle);
	if(this->window != NULL) {
		ImageWindow::destroy();
	}

}

/*static*/ void ImagaManager::onDataAvailable(xn::ProductionNode& node, void* cookie) {
//	ImagaManager* tthis = static_cast<ImagaManager*>(cookie);
	// FIXME!!!!
//    tthis->imageGenerator.GetMetaData(tthis->recentImageData);
}

}
