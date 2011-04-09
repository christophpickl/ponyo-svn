#include <ponyo/pnopenni/simplified/ContextX.hpp>
#include <ponyo/pnjna/PnJNA.hpp>


pn::ContextX* context;

extern "C" void startup() {
	context = new pn::ContextX();
	context->init();
	context->start();
}
extern "C" void destroy() {
	context->shutdown();
	delete context;
}
