#include <ponyo/jna/PnJNA.hpp>

using namespace pn;

Log* LOG = NEW_LOG();

OpenNIFacade* facade;

extern "C" void pnStartWithXml(const char* configPath, UserStateCallback userStateCallback, JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartWithXml(configPath)");
	__pnStart(configPath, true, userStateCallback, jointPositionCallback);
}

extern "C" void pnStartRecording(const char* oniFilePath, UserStateCallback userStateCallback, JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartRecording(oniFilePath)");
	__pnStart(oniFilePath, false, userStateCallback, jointPositionCallback);
}

void __pnStart(const char* configOrOniFile, bool isConfigFlag, UserStateCallback userStateCallback, JointPositionCallback jointPositionCallback) {
	if(facade != NULL) {
		// FIXME throw an IllegalStateException!!!
	}
	try {
		facade = new OpenNIFacade(userStateCallback, jointPositionCallback);
		if(isConfigFlag) {
			facade->startWithXml(configOrOniFile); // "misc/playground_config.xml"
		} else {
			facade->startRecording(configOrOniFile); // "/myopenni/myoni.oni"
		}

		// FIXME proper error handling! => additional callback for exceptions (also use for background thread)
	} catch(const OpenNiException& e) {
		e.printBacktrace();
	} catch(const Exception& e) {
		e.printBacktrace();
	} catch (const std::exception& e) {
		fprintf(stderr, "std exception: %s\n", e.what());
	} catch (...) {
		fprintf(stderr, "Unhandled exception!");
	}
}

extern "C" void pnDestroy() {
	LOG->info("pnDestroy()");

	printf("foooo #1\n");
	if(facade != NULL) {
	printf("foooo #2\n");
		facade->destroy();
	printf("foooo #3\n");
//		delete facade;
	printf("foooo #4\n");
	}
}

