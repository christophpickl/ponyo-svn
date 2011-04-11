#include <stdio.h>
#include <ponyo/samples/PlaygroundSample.hpp>

namespace pn {

Log* PlaygroundSample::LOG = NEW_LOG();

PlaygroundSample::PlaygroundSample() {
	LOG->debug("new PlaygroundSample()");
}

PlaygroundSample::~PlaygroundSample() {
	LOG->debug("~PlaygroundSample()");
}
}

using namespace pn;

int main() {
	printf("main() START\n");

	PlaygroundSample* sample = new PlaygroundSample();

	delete sample;

	printf("main() END\n");
	return 0;
}
