#pragma once
#ifndef CAMINITIALIZER_HPP_
#define CAMINITIALIZER_HPP_

#include <boost/thread.hpp>
#include <ponyo/pncommon/Async.hpp>
#include <ponyo/pnopenni/common_openni.hpp>
#include <ponyo/pnopenni/multiple/CamInitializerListener.hpp>
#include <ponyo/pnopenni/multiple/CamInitializerException.hpp>
#include <ponyo/pnopenni/CamInitDescriptor.hpp>
#include <ponyo/pnopenni/UserManager.hpp>

namespace pn {
class CamInitializer : public Async<CamInitializerListener*> {
public:
	CamInitializer(UserManager*);
	virtual ~CamInitializer();

	/**
	 * Result will be returned async via registered CamInitializerListeners.
	 */
	void fetchDevices/*start()*/(xn::Context& context, CamInitDescriptor*);
private:
	static Log* LOG;
	boost::thread workerThread;
	UserManager* userManager;
	CamInitDescriptor* initDescriptor;

	void run(xn::Context&);
	void loadDeviceInfos(std::vector<xn::NodeInfo>& deviceInfos, xn::NodeInfoList& deviceInfoList, xn::Context&);
	void initUserManager(xn::Context&) throw(CamInitializerException);;
	Cam* createCamInstance(xn::ImageGenerator&, xn::NodeInfo& deviceInfo);
	void dispatchOnInitializedCams(std::vector<Cam*>&);
	void dispatchOnException(Exception&);
};
}

#endif // CAMINITIALIZER_HPP_



