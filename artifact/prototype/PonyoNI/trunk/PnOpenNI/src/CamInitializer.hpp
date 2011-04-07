#pragma once
#ifndef CAMINITIALIZER_HPP_
#define CAMINITIALIZER_HPP_

#include <boost/thread.hpp>
#include "common_openni.hpp"
#include "Async.hpp"
#include "CamInitializerListener.hpp"
#include "CamInitializerException.hpp"
#include "CamInitDescriptor.hpp"
#include "UserManager.hpp"

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



