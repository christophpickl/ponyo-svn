#include <stdio.h>
#include <stdlib.h> // exit()
#include "log/LogFactory.hpp"
#include "common.hpp"
#include "Exception.hpp"
#include "MainWindow.hpp"
#include "MainWindowListener.hpp"
#include "OpenNiManager.hpp"

using namespace pn;

Log* LOG_APP = NEW_LOG(__FILE__)

class App : public MainWindowListener {
public:
	App() :
		window(new MainWindow()),
		manager(new OpenNiManager()) {
		LOG_APP->debug("new App()");

		this->window->addListener(this);
	}

	~App() {
		LOG_APP->debug("~App()");

		this->window->removeListener(this);

		delete this->window;
		delete this->manager;
	}

	void main(int argc, char** argv) {
		LOG_APP->info("main()");

		this->window->init(argc, argv);
		this->window->display();
	}

	void onListDevices() {
		LOG_APP->info("monListDevicesain()");
//		this->manager->
	}

	void onQuit() {
		LOG_APP->info("onQuit()");
		exit(1);
	}

private:
	MainWindow* window;
	OpenNiManager* manager;
	static Log LOG;

};

int main(int argc, char** argv) {
	println("main() START");

	App app;
	app.main(argc, argv);

	println("main() END");
	return 0;
}

