#include <stdio.h>
#include <stdlib.h> // exit()

#include "common.hpp"
#include "pninclude_opencv.h"
#include "MainWindow.hpp"
#include "OpenNiManager.hpp"

#define IMG_BLUE_TEMPLATE "images/blue_ball_template.jpg"
#define IMG_RED_TEMPLATE "images/red_ball_template.jpg"
#define IMG_TEMPLATE IMG_BLUE_TEMPLATE

#define CAM_ENABLE_IMAGE_GENERATOR true
#define CAM_ENABLE_USER_GENERATOR false
#define IMAGE_OUTPUT_MODE_WIDTH 640
#define IMAGE_OUTPUT_MODE_HEIGHT 480
#define IMAGE_OUTPUT_MODE_FPS 30

/*

l ... load devices
s ... start generating
c ... calibrate
q ... quit

 */
using namespace pn;

Log* LOG_APP = NEW_LOG(__FILE__)

class App : public MainWindowListener {
public:
	App(const char* pTemplateImagePath) : templateImagePath(pTemplateImagePath) {
		LOG_APP->debug("new App(templateImagePath)");

		this->imageOutputMode.nXRes = IMAGE_OUTPUT_MODE_WIDTH;
		this->imageOutputMode.nYRes = IMAGE_OUTPUT_MODE_HEIGHT;
		this->imageOutputMode.nFPS = IMAGE_OUTPUT_MODE_FPS;

		this->initDescriptor = new CamInitDescriptor(
			/*imageGeneratorRequired*/ CAM_ENABLE_IMAGE_GENERATOR,
			/*userGeneratorRequired*/ CAM_ENABLE_USER_GENERATOR,
			this->imageOutputMode
		);
	}

	~App() {
		LOG_APP->debug("~App()");

		this->window->removeListener(this);

		delete this->window;
		delete this->manager;

		delete this->camInitializer;
		delete this->userManager;

		delete this->camCalibrator;
		delete this->imageSaver;
		delete this->imageConverter;
		delete this->imageDetector;
		delete this->templateImage;
	}

	void main(int argc, char** argv) {
		LOG_APP->info("main()");

		this->initAndWireObjects();
		if(this->manager == NULL) { fprintf(stderr, "This happens when initAndWireObjects() was not invoked before ;)"); return; }

		LOG_APP->trace(">> this->window->init(argc, argv);");
		this->window->init(argc, argv);
		LOG_APP->trace(">> this->manager->init();");
		this->manager->init();
		LOG_APP->trace(">> this->window->display();");
		this->window->display();
	}

	void onListDevices() {
		LOG_APP->info("onListDevices()");

		try {
			this->manager->listDevices(this->initDescriptor);
		} catch (Exception& e) {
			fprintf(stderr, "Exception was thrown while listing devices: %s\n", e.getMessage());
			e.printBacktrace();
		}
	}

	void onStartGenerating() {
		LOG_APP->info("onStartGenerating()");

		try {
			this->manager->startAll();
		} catch (Exception& e) {
			fprintf(stderr, "Exception was thrown while starting up: %s\n", e.getMessage());
			e.printBacktrace();
		}
	}

	void onCalibrate() {
		LOG_APP->info("onCalibrate()");
		try {
			this->manager->calibrate();
		} catch (Exception& e) {
			fprintf(stderr, "Exception was thrown while calibrating: %s\n", e.getMessage());
			e.printBacktrace();
		}
	}

	void onQuit() {
		LOG_APP->info("onQuit()");
		this->manager->shutdown();
		exit(0);
	}

private:
	/** configuration value */
	XnMapOutputMode imageOutputMode;
	/** configuration value */
	CamInitDescriptor* initDescriptor;

	MainWindow* window;
	OpenNiManager* manager;

	CamInitializer* camInitializer;
	UserManager* userManager;

	CamCalibrator* camCalibrator;
	ImageDetector* imageDetector;
	ImageConverter * imageConverter;
	ImageSaver* imageSaver;
	cv::Mat* templateImage;
	const char* templateImagePath;

	void initAndWireObjects() {
		LOG_APP->debug("initAndWireObjects()");
		this->templateImage = new cv::Mat(cvLoadImage(this->templateImagePath)); // TODO store PImage* and cvReleaseImage afterwards?

		// wire objects
		this->imageDetector = new ImageDetector();
		this->imageConverter = new ImageConverter();
		this->imageSaver = new ImageSaver(this->imageConverter);
		this->camCalibrator = new CamCalibrator(this->imageDetector, this->imageConverter, this->imageSaver, this->templateImage);

		this->userManager = new UserManager();
		this->camInitializer = new CamInitializer(this->userManager);
		this->manager = new OpenNiManager(this->camInitializer, this->camCalibrator, this->userManager);

		this->window = new MainWindow();
		this->window->addListener(this);
	}

};

int main(int argc, char** argv) {
	println("main() START");

	App app(IMG_TEMPLATE);
	app.main(argc, argv);

	println("main() END");
	return 0;
}

