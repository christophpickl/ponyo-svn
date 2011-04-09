#include <stdio.h>
#include <iostream>
#include <boost/thread.hpp>
#include <boost/date_time.hpp>

// see: http://antonym.org/2009/05/threading-with-boost---part-i-creating-threads.html

bool shouldQuit = false;
class Foo {
public:
	Foo() {
		printf("new Foo()\n");
	}
	~Foo() {
		printf("~Foo()\n");
	}

	void onThread() {
		printf("Foo.onThread() START\n");
		boost::posix_time::seconds workTime(1);

		while(!shouldQuit) {
			printf("Foo WORKING again!\n");
			boost::this_thread::sleep(workTime);
		}

		printf("Foo.onThread() END\n");
	}
};

int main(int argc, char* argv[]) {
	std::cout << "main() START" << std::endl;

	Foo* f = new Foo();
	printf("main() ... spawning new thread\n");
	boost::thread someThread(&Foo::onThread, f);

	printf("main() sleeping\n");
	boost::posix_time::seconds sleepTime(3);
	boost::this_thread::sleep(sleepTime);
	printf("main() aborting\n");
	shouldQuit = true;

	printf("main() ... someThread.join()\n");
	someThread.join();

	delete f;

	std::cout << "main() END" << std::endl;
	return 0;
}
