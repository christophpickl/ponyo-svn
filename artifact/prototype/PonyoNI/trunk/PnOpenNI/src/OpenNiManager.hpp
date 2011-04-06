#pragma once
#ifndef OPENNIMANAGER_HPP_
#define OPENNIMANAGER_HPP_

#include "common_openni.hpp"
#include "DeviceInitializer.hpp"

namespace pn {
class OpenNiManager {
public:
	OpenNiManager(DeviceInitializer*);
	virtual ~OpenNiManager();

	void init() throw (OpenNiException);
	void listDevices();
	void shutdown();

private:
	DeviceInitializer* initializer;
	xn::Context context;
	static Log* LOG;
};
}

#endif // OPENNIMANAGER_HPP_
