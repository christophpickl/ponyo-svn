#pragma once
#ifndef GLUTTHREAD_HPP_
#define GLUTTHREAD_HPP_

#include <boost/thread.hpp>
#include <ponyo/openni/includes/headers_glut.hpp>
#include <ponyo/PnCommon.hpp>

namespace pn {
class GlutThread {
public:
	GlutThread();
	virtual ~GlutThread();

	void start();
	void stop();

private:
	static Log* LOG;
	boost::thread mainLoopThread;

	void onThreadRun();
};
}

#endif // GLUTTHREAD_HPP_
