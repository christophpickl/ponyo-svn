#include <stdio.h>
#include <iostream>
#include <boost/thread.hpp>
#include <boost/date_time.hpp>


bool shouldQuit = false;

class Foo {
public:
	Foo() {
		printf("new Foo()\n");
	}
	~Foo() {
		printf("~Foo()\n");
	}
//		while(!shouldQuit) {
//			boost::posix_time::seconds workTime(1);
//			boost::this_thread::sleep(workTime);
//		}
//		boost::thread someThread(Foo::onThreadRun);
	void operator()() {
		printf("Foo.operator() START\n");
			boost::posix_time::seconds workTime(2);
			boost::this_thread::sleep(workTime);
		printf("Foo.operator() END\n");
	}
};

int main(int argc, char* argv[]) {
	std::cout << "main() START" << std::endl;

//	boost::thread workerThread(workerFunc, 3);
//	std::cout << "main: waiting for thread" << std::endl;
//	workerThread.join();

	Foo f;
	boost::thread someThread(f);
	someThread.join();

	std::cout << "main() END" << std::endl;
	return 0;
}
