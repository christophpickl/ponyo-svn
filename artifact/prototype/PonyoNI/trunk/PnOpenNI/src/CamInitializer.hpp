#pragma once
#ifndef CAMINITIALIZER_HPP_
#define CAMINITIALIZER_HPP_

#include <boost/thread.hpp>
#include "common_openni.hpp"
#include "Async.hpp"
#include "CamInitializerListener.hpp"

namespace pn {
class CamInitializer : public Async<CamInitializerListener*> {
public:
	CamInitializer();
	virtual ~CamInitializer();

	void fetchDevices/*start()*/(xn::Context&);
private:
	static Log* LOG;
	boost::thread workerThread;

	void run(xn::Context& context);
	void dispatchEvent(std::vector<Cam*>&);
};
}

#endif // CAMINITIALIZER_HPP_
