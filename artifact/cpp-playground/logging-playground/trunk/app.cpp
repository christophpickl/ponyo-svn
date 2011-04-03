#include <stdio.h>
#include "LogFactory.hpp"
#include "Foobar.hpp"

Log* LOG = LogFactory::getLog(__FILE__);

int main() {
	LOG->debug("main()");

	Foobar* foo = new Foobar();
	foo->doit();
	delete foo;

	LogFactory::deleteLogInstances();

	return 0;
}
