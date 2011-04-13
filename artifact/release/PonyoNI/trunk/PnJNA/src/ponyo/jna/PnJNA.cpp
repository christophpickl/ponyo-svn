#include <ponyo/jna/PnJNA.hpp>

using namespace pn;

Log* LOG = NEW_LOG();
OpenNIFacade* facade;
bool isRunning = false;

extern "C" void pnStartWithXml(
		const char* configPath,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartWithXml(configPath)");
	__pnStart(configPath, true, userStateCallback, jointPositionCallback);
}

extern "C" void pnStartRecording(
		const char* oniFilePath,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartRecording(oniFilePath)");
	__pnStart(oniFilePath, false, userStateCallback, jointPositionCallback);
}

void __pnStart(
		const char* configOrOniFile,
		bool isConfigFlag,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
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
		isRunning = true;
		// TODO proper error handling! => additional callback for exceptions (also use for background thread)
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

	// FIXME check if currently initializing, as client (awt dispatcher thread) could be async call
	if(facade != NULL) {
		printf("destroying; is running = %i\n", isRunning);
		if(isRunning == true) {
			facade->destroy();
			isRunning = true;
		}
		delete facade;
	} else {
		LOG->warn("nothing to destroy");
	}
}

