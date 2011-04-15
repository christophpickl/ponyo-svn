#include <boost/thread.hpp>
#include <ponyo/PnOpenNI.hpp>

namespace pn {

class WindowedSample {

public:
	WindowedSample() { }
	~WindowedSample() { }
	void start() {
		LOG->info("start()");

//		OpenNIUtils::enableXnLogging(XN_LOG_INFO);

		StartXmlConfig config("misc/playground_config.xml", &WindowedSample::onUserStateChanged, &WindowedSample::onJointPositionChanged);
		config.setImageGeneratorEnabled(true);
		config.setDepthGeneratorEnabled(false);
		config.setUserGeneratorEnabled(false);
		WindowedSample::facade.startWithXml(config);

		LOG->debug("Spawning new thread ...");
		WindowedSample::secondMainThread = boost::thread(&WindowedSample::onThreadRun);

		printf("Displaying window (gets stuck in glut mainloop) ...\n");
		WindowedSample::facade.setWindowVisible(true);
	}

private:
	static Log* LOG;
	static OpenNIFacade facade;
	static boost::thread secondMainThread;
	static int jointCounter; // to print out every 100th joint change only


	static void onUserStateChanged(UserId userId, UserState userState) {
		printf(">>>>> PlaygroundSample says: onUserStateChanged(userId=%i, userState=%i)\n", userId, userState);
	}

	static void onJointPositionChanged(UserId userId, unsigned int jointId, float x, float y, float z) {
		if(++WindowedSample::jointCounter == 100) {
			printf(">>>>> PlaygroundSample says: 100th onJointPositionChanged(userId=%i, jointId=%i, ...)\n", userId, jointId);
			WindowedSample::jointCounter = 0;
		}
	}

	static void tearDown() {
		LOG->info("tearDown()");
		WindowedSample::facade.shutdown();

		LOG->debug("invoking exit(0), as glut could still block main loop");
		exit(0);
	}

	static void onThreadRun() {
		LOG->info("onThreadRun()");

		// FIXME this does not work yet :(
//		CommonUtils::sleep(2);
//		g_facade.setWindowVisible(false);
//		CommonUtils::sleep(2);
//		printf("XXXXXXXXXXXXXXXXXXXXXXXXXX reshowing window\n");
//		g_facade.setWindowVisible(true);
//		printf("XXXXXXXXXXXXXXXXXXXXXXXXXX reshowing window END\n");
//		CommonUtils::sleep(2);
//		g_facade.setWindowVisible(false);

		printf("Hit ENTER to quit\n");
		CommonUtils::waitHitEnter(false);
		printf("ENTER pressed, shutting down.\n");

		WindowedSample::tearDown();
	}

	static void onWindowAction(WindowAction actionId) {
		LOG->debug2("onWindowAction(actionId=%i)", actionId);
		switch(actionId) {
		case WINDOW_ACTION_ESCAPE:
			WindowedSample::tearDown();
			break;

		default:
			LOG->warn2("Unhandled window action [%i]!", actionId);
		}
	}
};
Log* WindowedSample::LOG = NEW_LOG();
OpenNIFacade WindowedSample::facade;
boost::thread WindowedSample::secondMainThread;
int WindowedSample::jointCounter = 0;

}


int main(int argc, char** argv) {
	printf("WindowedSample main() START\n");

//	ImageWindow* win = ImageWindow::getInstance(&onWindowAction);
//	win->init(argc, argv);
//	win->setWindowVisible(true);
//	ImageWindow::destroy();

	pn::WindowedSample* sample = new pn::WindowedSample();
	try {
		sample->start();
	} catch(pn::Exception& e) {
		e.printBacktrace();
	}
	delete sample;

	printf("main() END\n");
	return 0;
}
