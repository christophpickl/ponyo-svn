#include <stdio.h>
#include "log/LogFactory.hpp"
#include "common.hpp"
#include "Exception.hpp"
#include "MainWindow.hpp"

using namespace pn;

NEW_LOG(__FILE__)

class App {
public:
	App() : window(new MainWindow()) {
		LOG->debug("new App()");
	}
	~App() {
		LOG->debug("~App()");
		delete this->window;
	}

	void main(int argc, char** argv) {
		LOG->info("main()");

		this->window->startInfiniteLoop(argc, argv);
	}

private:
	MainWindow* window;

};

int main(int argc, char** argv) {
	println("main() START");

	App app;
	app.main(argc, argv);

	println("main() END");
	return 0;
}

