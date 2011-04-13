#include <ponyo/jna/PnJNA.hpp>

using namespace pn;

Log* LOG = NEW_LOG();
OpenNIFacade facade;
bool isRunning = false;

extern "C" int pnStartByXmlConfig(
		const char* configXmlPath,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartByXmlConfig(configXmlPath)");
	return __pnStart(configXmlPath, true, userStateCallback, jointPositionCallback);
}

extern "C" int pnStartByOniRecording(
		const char* recordingOniPath,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartByOniRecording(recordingOniPath)");
	return __pnStart(recordingOniPath, false, userStateCallback, jointPositionCallback);
}

int __pnStart(
		const char* configOrOniPath,
		bool isConfigFlag,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {

	if(isRunning == true) {
		return 67;
	}

	int resultCode = -1;
	try {
		if(isConfigFlag) {
			facade.startWithXml(configOrOniPath, userStateCallback, jointPositionCallback);
		} else {
			facade.startRecording(configOrOniPath, userStateCallback, jointPositionCallback);
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

extern "C" void pnShutdown() {
	LOG->info("pnShutdown()");

	// FIXME check if currently initializing, as client (awt dispatcher thread) could be async call
	printf("shutting down; is running = %i\n", isRunning);
	if(isRunning == true) {
		facade.shutdown();
		isRunning = false;
	} else {
		LOG->warn("nothing to shutdown");
	}
}

