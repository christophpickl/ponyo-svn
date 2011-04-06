#pragma once
#ifndef DEVICEINITIALIZER_HPP_
#define DEVICEINITIALIZER_HPP_

#include "common_openni.hpp"

namespace pn {
class DeviceInitializer {
public:
	DeviceInitializer();
	virtual ~DeviceInitializer();

	void fetchDevices(xn::Context&);
private:
	static Log* LOG;
};
}

#endif // DEVICEINITIALIZER_HPP_
