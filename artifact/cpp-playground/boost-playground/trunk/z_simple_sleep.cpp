
#include <iostream>
#include <boost/thread.hpp>
#include <boost/date_time.hpp>

void workerFunc(int delaySecs) {
	boost::posix_time::seconds workTime(delaySecs);
	std::cout << "Worker: running" << std::endl;
	boost::this_thread::sleep(workTime); // Pretend to do something useful...
	std::cout << "Worker: finished" << std::endl;
}

int main(int argc, char* argv[]) {
	std::cout << "main: startup" << std::endl;
	boost::thread workerThread(workerFunc, 3);
	std::cout << "main: waiting for thread" << std::endl;

	workerThread.join();
	std::cout << "main: done" << std::endl;
	return 0;
}
