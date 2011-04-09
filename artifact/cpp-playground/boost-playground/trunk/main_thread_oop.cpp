#include <stdio.h>
#include <iostream>
#include <boost/thread.hpp>
#include <boost/date_time.hpp>

// see: http://antonym.org/2009/05/threading-with-boost---part-i-creating-threads.html

class Foo {
	public:
		Foo() : shouldRun(true) {
			printf("new Foo()\n");
		}
		~Foo() {
			printf("~Foo()\n");
		}
		void doit() {
			printf("Foo.doit() START\n");
			this->updateThread = boost::thread(&Foo::onThread, this);
			printf("Foo.doit() END\n");
		}
		void stopAndJoin() {
			printf("Foo.onstopAndJoin() START\n");
			this->shouldRun = false;
			this->updateThread.join();
			printf("Foo.onstopAndJoin() END\n");
		}
		void onThread() {
			printf("Foo.onThread() START\n");
			boost::posix_time::seconds workTime(1);
			while(this->shouldRun) {
				printf("Foo WORKING again!\n");
				boost::this_thread::sleep(workTime);
			}
			printf("Foo.onThread() END\n");
		}
	private:
		boost::thread updateThread;
		bool shouldRun;
};

int main(int argc, char* argv[]) {
	std::cout << "main() START" << std::endl;

	Foo* f = new Foo();
	printf("main() ... spawning new thread\n");
	f->doit();

	printf("main() sleeping\n");
	boost::posix_time::seconds sleepTime(5);
	boost::this_thread::sleep(sleepTime);
	printf("main() aborting\n");

	f->stopAndJoin();
	delete f;
	std::cout << "main() END" << std::endl;
	return 0;
}
