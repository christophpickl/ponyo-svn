#include <ponyo/jna/PnJNA.hpp>

using namespace pn;

Log* LOG = NEW_LOG();
OpenNIFacade g_facade;
bool g_isRunning = false;
bool g_isStartingUp = false; // TODO maybe rework with mutex!

// TODO create struct for each Config type which can be used to create proper Object type (instead of flat struct)
//      ==> especially to provide java API (in java, provide client again same high level Object type); struct could also used by ponyo c++ users
//           ... @java api translation: happens all the time, flattening (struct) and high-leveling (oop) again, as JNA/JNI knows no OOP

// TODO should we add shutdown signal hooks?? maybe do it in java only?!

extern "C" int pnStartByXmlConfig(
		const char* configXmlPath,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartByXmlConfig(..)");
	return __pnStart(configXmlPath, true, userStateCallback, jointPositionCallback);
}

extern "C" int pnStartByOniRecording(
		const char* recordingOniPath,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {
	LOG->info("pnStartByOniRecording(..)");
	return __pnStart(recordingOniPath, false, userStateCallback, jointPositionCallback);
}

int __pnStart(
		const char* configOrOniPath,
		bool isConfigFlag,
		UserStateCallback userStateCallback,
		JointPositionCallback jointPositionCallback) {

	if(g_isStartingUp == true) { // multithreaded tried to invoke start multiple times!
		return 68;
	}
	if(g_isRunning == true) {
		return 67;
	}

	g_isStartingUp = true;

	int resultCode = -1;
	try {
		if(isConfigFlag) {

			StartXmlConfig config(configOrOniPath, userStateCallback, jointPositionCallback);
			config.setMirrorModeEnabled(true);
			config.setImageGeneratorEnabled(true); // TODO should autodetect itself (may overrule is possible)
			g_facade.startWithXml(config);

		} else {
			StartOniConfig config(configOrOniPath, userStateCallback, jointPositionCallback);
			g_facade.startRecording(config);

		}
		g_isRunning = true;
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
	g_isStartingUp = false;
	return resultCode;
}

extern "C" void pnShutdown() {
	LOG->info("pnShutdown()");
	if(g_isStartingUp == true) {
		// FIXME return some errorcode!!!
		fprintf(stderr, "tried to shutdown while starting up!\n");
		return;
	}

	// FIXME check if currently initializing, as client (awt dispatcher thread) could be async call
	// LOG->debug2("shutting down; is running = %s\n", boolToString(g_isRunning));
	if(g_isRunning == true) {
		g_facade.shutdown();
		g_isRunning = false;
	} else {
		LOG->warn("nothing to shutdown");
	}
}

extern "C" const char* pnGetPonyoVersion() {
	return CommonUtils::getPonyoVersion();
}

extern "C" const char* pnGetOpenNIVersion() {
	return CommonUtils::getOpenNIVersion();
}

