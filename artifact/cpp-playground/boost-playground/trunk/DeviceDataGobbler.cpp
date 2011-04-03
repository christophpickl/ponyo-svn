#include <stdio.h>
#include "DeviceDataGobbler.hpp"

DeviceDataGobbler::DeviceDataGobbler(DataSnapshot* pData) : data(pData) {
	printf("new DeviceDataGobbler(data)\n");
}

DeviceDataGobbler::~DeviceDataGobbler() {
	printf("~DeviceDataGobbler()\n");
}

/*public*/ void DeviceDataGobbler::start() {
	printf("DeviceDataGobbler.start()\n");
	this->myThread = boost::thread(&DeviceDataGobbler::run, this/*optional arguments*/);
}

/*private*/ void DeviceDataGobbler::run() {
	printf("DeviceDataGobbler.run() ... working\n");

	int i = 1;
	while(this->isStopped == false) {
		printf("DeviceDataGobbler.run() ... sleeping\n");
		boost::this_thread::sleep(boost::posix_time::milliseconds(GOBBLER_DELAY_WORK));

		printf("DeviceDataGobbler.run() ... data->lock()\n");
		this->data->lock();
		boost::this_thread::sleep(boost::posix_time::milliseconds(GOBBLER_DELAY_WRITE_DATA));
		this->data->setNumber(i++);
		this->data->unlock();
	}

	printf("DeviceDataGobbler.run() ... work ended\n");
}

/*public*/ void DeviceDataGobbler::stop() {
	printf("DeviceDataGobbler.stop()\n");
	this->isStopped = true;
}

/*public*/ void DeviceDataGobbler::join() {
	printf("DeviceDataGobbler.join()\n");
	this->myThread.join();
	printf("DeviceDataGobbler.join() ... finished\n");
}
