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

	void fetchDevices/*start()*/(xn::Context& context);
private:
	static Log* LOG;
	boost::thread workerThread;

	void run(xn::Context& context);
	void loadDeviceInfos(std::vector<xn::NodeInfo>& deviceInfos, xn::Context& context);
	Cam* createCamInstance(xn::ImageGenerator& imageGenerator, xn::NodeInfo& deviceInfo);
	void dispatchEvent(std::vector<Cam*>& cams);
};
}

#endif // CAMINITIALIZER_HPP_
