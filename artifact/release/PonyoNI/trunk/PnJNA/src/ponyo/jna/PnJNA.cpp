#include <ponyo/jna/PnJNA.hpp>

using namespace pn;

Log* LOG = NEW_LOG();
OpenNIFacade facade;
bool isRunning = false;

// TODO create struct for each Config type which can be used to create proper Object type (instead of flat struct)
//      ==> especially to provide java API (in java, provide client again same high level Object type); struct could also used by ponyo c++ users
//           ... @java api translation: happens all the time, flattening (struct) and high-leveling (oop) again, as JNA/JNI knows no OOP

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
			fprintf(stderr, "TODOOOOOO in PnJNA!\n");
			// FIXME !!! facade.startWithXml(configOrOniPath, userStateCallback, jointPositionCallback);
		} else {
			fprintf(stderr, "TODOOOOOO in PnJNA!\n");
			// FIXME !!! facade.startRecording(configOrOniPath, userStateCallback, jointPositionCallback);
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

