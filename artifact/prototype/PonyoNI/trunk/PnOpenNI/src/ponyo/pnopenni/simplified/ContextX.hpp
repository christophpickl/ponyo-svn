#pragma once
#ifndef CONTEXTX_HPP_
#define CONTEXTX_HPP_

#include <boost/thread.hpp>
#include <boost/date_time.hpp>

#include <ponyo/pnopenni/PnOpenNI.hpp>
#include <ponyo/pnopenni/UserManager.hpp>
#include <ponyo/pnopenni/simplified/ContextXListener.hpp>

namespace pn {
class ContextX : public Async<ContextXListener*> {
public:
	ContextX();
	~ContextX();

	void init() throw(OpenNiException);
	void start() throw(OpenNiException);
	void startRecording(const XnChar* oniFilePath) throw(OpenNiException);

	void addUserManagerListener(UserManagerListener*);

	void waitAndUpdate();
	void shutdown();

private:
	static Log* LOG;
	xn::Context context;
	UserManager* userManager;
	XnMapOutputMode mapMode;
	xn::DepthGenerator depthGenerator;
	boost::thread updateThread;
	bool threadShouldRun;

	void startInternal(bool shouldSetOutputMode) throw(OpenNiException);
	void onThreadRun();
	void broadcastThreadException(Exception&);
	// TODO block assign-operator and copy-ctor
};
}

#endif // CONTEXTX_HPP_
