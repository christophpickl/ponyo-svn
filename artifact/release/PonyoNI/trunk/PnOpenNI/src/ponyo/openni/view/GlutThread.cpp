#include <ponyo/openni/view/GlutThread.hpp>

namespace pn {

Log* GlutThread::LOG = NEW_LOG();

GlutThread::GlutThread() {
	LOG->debug("new GlutThread()");
}

GlutThread::~GlutThread() {
	LOG->debug("~GlutThread()");
}

/*public*/ void GlutThread::start() {
	LOG->debug("start()");
	this->mainLoopThread = boost::thread(&GlutThread::onThreadRun, this);
}

/*public*/ void GlutThread::stop() {
	LOG->debug("stop()");

	// FIXME what to do? interrupt?? or... WHAT?!
}

/*private*/ void GlutThread::onThreadRun() {
	LOG->debug("onThreadRun() ... entering glut main loop");
	glutMainLoop();
	LOG->fatal("onThreadRun() ... END // TODO i think this one is never reached, isnt it?");
}

}
