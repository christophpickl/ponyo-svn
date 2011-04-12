#include <ponyo/openni/PnOpenNI.hpp>

int main() {
	printf("SimpleSample main() START\n");

	pn::OpenNIFacade* facade = new pn::OpenNIFacade();
	try {
//		facade->startWithXml("misc/playground_config.xml");
		facade->startRecording("/myopenni/myoni.oni");
	} catch(const pn::Exception& e) {
//	} catch(const pn::OpenNiException& e) {
		e.printBacktrace();
	}
//	} catch (const std::exception& e) {
//	} catch (...) {
//	}
	facade->destroy();
	delete facade;

	printf("main() END\n");
	return 0;
}
