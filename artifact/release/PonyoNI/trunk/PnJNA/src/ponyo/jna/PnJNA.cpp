#include <ponyo/jna/PnJNA.hpp>

using namespace pn;

Log* LOG = NEW_LOG();
OpenNIFacade* facade;
bool isRunning = false;

extern "C" int pnStartWithXml(
		const char* configPath,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartWithXml(configPath)");
	return __pnStart(configPath, true, userStateCallback, jointPositionCallback);
}

extern "C" int pnStartRecording(
		const char* oniFilePath,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartRecording(oniFilePath)");
	return __pnStart(oniFilePath, false, userStateCallback, jointPositionCallback);
}

int __pnStart(
		const char* configOrOniFile,
		bool isConfigFlag,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
	if(facade != NULL) {
		return 66; // TODO return error resultCode -> in java throw an IllegalStateException!!!
	}
	if(isRunning == true) {
		return 67;
	}

	int resultCode = -1;
	try {
		facade = new OpenNIFacade(userStateCallback, jointPositionCallback);
		if(isConfigFlag) {
			facade->startWithXml(configOrOniFile); // "misc/playground_config.xml"
		} else {
			facade->startRecording(configOrOniFile); // "/myopenni/myoni.oni"
		}
		isRunning = true;
		resultCode = 0;
		// TODO proper error handling! => additional callback for exceptions (also use for background thread)
	} catch(const OpenNiException& e) {
		e.printBacktrace();
		resultCode = 1;
	} catch(const Exception& e) {
		e.printBacktrace();
		resultCode = 2;
	} catch (const std::exception& e) {
		fprintf(stderr, "std exception: %s\n", e.what());
		resultCode = 3;
	} catch (...) {
		fprintf(stderr, "Unhandled exception!");
		resultCode = 4;
	}
	return resultCode;
}

extern "C" void pnDestroy() {
	LOG->info("pnDestroy()");

	// FIXME check if currently initializing, as client (awt dispatcher thread) could be async call
	if(facade != NULL) {
		printf("destroying; is running = %i\n", isRunning);
		if(isRunning == true) {
			facade->destroy();
			isRunning = false;
		}
		delete facade;
	} else {
		LOG->warn("nothing to destroy");
	}
}

