#include <stdio.h>
#include <stdlib.h> // exit()

#include "common.hpp"
#include "pninclude_opencv.h"
#include "MainWindow.hpp"
#include "OpenNiManager.hpp"

#define IMG_BLUE_TEMPLATE "images/blue_ball_template.jpg"
#define IMG_RED_TEMPLATE "images/red_ball_template.jpg"

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
	App() : window(new MainWindow()) {
		LOG_APP->debug("new App()");
	}

	~App() {
		LOG_APP->debug("~App()");

		this->window->removeListener(this);

		delete this->window;
		delete this->manager;
		delete this->camInitializer;
		delete this->camCalibrator;
		delete this->imageSaver;
		delete this->imageDetector;
//		delete this->templateImage;
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
		// TODO maybe spawn thread?!
		try {
			this->manager->listDevices();
		} catch (Exception& e) {
			fprintf(stderr, "Exception was thrown: %s\n", e.getMessage());
		}
	}

	void onStartGenerating() {
		LOG_APP->info("onStartGenerating()");
		this->manager->startGenerateImageForAllCams();
	}

	void onCalibrate() {
		LOG_APP->info("onCalibrate()");
		this->manager->calibrate();
	}

	void onQuit() {
		LOG_APP->info("onQuit()");
		this->manager->shutdown();
		exit(0);
	}

private:
	MainWindow* window;
	OpenNiManager* manager;
	CamInitializer* camInitializer;
	CamCalibrator* camCalibrator;
	ImageDetector* imageDetector;
	ImageSaver* imageSaver;
//	cv::Mat* templateImage;

	void initAndWireObjects() {
		LOG_APP->debug("initAndWireObjects()");
//		this->templateImage = new cv::Mat(cvLoadImage(IMG_BLUE_TEMPLATE));

		// wire objects
		this->imageDetector = new ImageDetector();
		this->imageSaver = new ImageSaver();
		this->camCalibrator= new CamCalibrator(this->imageDetector, this->imageSaver); //, this->templateImage);

		this->camInitializer = new CamInitializer();
		this->manager = new OpenNiManager(this->camInitializer, this->camCalibrator);

		this->window->addListener(this);
	}

};

int main(int argc, char** argv) {
	println("main() START");

	App app;
	app.main(argc, argv);

	println("main() END");
	return 0;
}

