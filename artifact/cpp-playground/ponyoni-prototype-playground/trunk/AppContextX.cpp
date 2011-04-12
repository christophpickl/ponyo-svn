#include <stdio.h>
#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include <ponyo/pnopenni/simplified/ContextX.hpp>

using namespace pn;
using namespace xn;

int main() {
	printf("main() START\n");

	printf("main() setting up context ...\n");
	ContextX* context = new ContextX();
	printf("main() init\n");
	context->init();
	printf("main() startRecording\n");
	context->startRecording("/myopenni/myoni.oni");
//	context->start();

	printf("main() sleeping ...\n");
	boost::posix_time::seconds workTime(20);
	boost::this_thread::sleep(workTime);

	printf("main() waking up and shutdown...\n");
	context->shutdown();
	delete context;

	printf("main() END\n");
	return 0;
}
