#include <stdio.h>
#include <stdlib.h> // exit()

#include "common.hpp"
#include "MainWindow.hpp"
#include "OpenNiManager.hpp"

using namespace pn;

Log* LOG_APP = NEW_LOG(__FILE__)

class App : public MainWindowListener {
public:
	App() : window(new MainWindow()) {
		LOG_APP->debug("new App()");

		this->deviceInitializer = new DeviceInitializer();
		this->manager = new OpenNiManager(this->deviceInitializer);

		this->window->addListener(this);
	}

	~App() {
		LOG_APP->debug("~App()");

		this->window->removeListener(this);

		delete this->window;
		delete this->manager;
		delete this->deviceInitializer;
	}

	void main(int argc, char** argv) {
		LOG_APP->info("main()");

		this->window->init(argc, argv);
		this->manager->init();
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

	void onQuit() {
		LOG_APP->info("onQuit()");
		this->manager->shutdown();
		exit(1);
	}

private:
	MainWindow* window;
	OpenNiManager* manager;
	DeviceInitializer* deviceInitializer;
	static Log LOG;

};

int main(int argc, char** argv) {
	println("main() START");

	App app;
	app.main(argc, argv);

	println("main() END");
	return 0;
}

