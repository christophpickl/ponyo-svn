#include <ponyo/openni/PnOpenNI.hpp>

int main() {
	printf("SimpleSample main() START\n");

	pn::PnContext* context = new pn::PnContext();
	try {
//		context->startWithXml("/myopenni/simple_config.xml");
		context->startRecording("/myopenni/myoni.oni");
	} catch(const pn::Exception& e) {
//	} catch(const pn::OpenNiException& e) {
		e.printBacktrace();
	}
//	} catch (const std::exception& e) {
//	} catch (...) {
//	}
	context->destroy();
	delete context;

	printf("main() END\n");
	return 0;
}
