#include <ponyo/openni/PnOpenNI.hpp>

int main() {
	printf("main() START\n");

	pn::PnContext* context = new pn::PnContext();

	try {
		context->startWithXml("");
	} catch(const pn::Exception& e) {

	}
//	} catch (const std::exception& e) {
//	} catch (...) {
//	}

	context->destroy();
	delete context;

	printf("main() END\n");
	return 0;
}
