#pragma once
#ifndef DEVICEDATAGOBBLER_H_
#define DEVICEDATAGOBBLER_H_

#define GOBBLER_DELAY_WORK 3000
#define GOBBLER_DELAY_WRITE_DATA 2000

#include <boost/thread.hpp>
#include "DataSnapshot.hpp"

class DeviceDataGobbler {
public:
	DeviceDataGobbler(DataSnapshot*);
	virtual ~DeviceDataGobbler();

	void start();
	void stop();
	void join();

private:
	boost::thread myThread;

	DataSnapshot* data;
	bool isStopped;

	void run();

};

#endif /* DEVICEDATAGOBBLER_H_ */
