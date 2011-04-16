#include <ponyo/PnOpenNI.hpp>

namespace pn {
class StartRecordingSample {
public:
	static void start(const char* oniRecordingPath) {
		LOG->info2("start(oniRecordingPath=%s)", oniRecordingPath);

		StartOniConfig config(oniRecordingPath, &StartRecordingSample::onUser, &StartRecordingSample::onJoint);
		config.setAsyncExceptionCallback(&StartRecordingSample::onAsyncException);

		facade.startRecording(config);

		CommonUtils::waitHitEnter();

		facade.shutdown();
	}
private:
	static Log* LOG;
	static OpenNIFacade facade;

	static void onAsyncException(const char* message, Exception& exception) {
		fprintf(stderr, "Async Exception: %s\n", message);
		exception.printBacktrace();
		facade.shutdown();
	}
	static void onUser(UserId id, UserState state) { }
	static void onJoint(UserId id, unsigned int jointId, float x, float y, float z) { }
};
Log* StartRecordingSample::LOG = NEW_LOG();
OpenNIFacade StartRecordingSample::facade;
}

int main(int argc, char** argv) {
	printf("main() START\n");

	try {
		pn::StartRecordingSample::start("/ponyo/oni.oni");
	} catch(pn::Exception& e) {
		e.printBacktrace();
	}

	printf("main() END\n");
	return 0;
}
