#include <stdio.h>

#include "LogFactory.hpp"
#include "Foobar.hpp"

Log* Foobar::LOG = LogFactory::getLog(__FILE__);

Foobar::Foobar() {

}

void Foobar::doit() {
	LOG->debug("doit()");
}

Foobar::~Foobar() {
	// TODO Auto-generated destructor stub
}
