#include "MultipleKinects.h"
#include "ponyo_common.h"


MultipleKinects::MultipleKinects() {
	printf("new MultipleKinects()\n");
}

MultipleKinects::~MultipleKinects() {
	printf("delete ~MultipleKinects()\n");
}


void MultipleKinects::initFromXml(std::string xmlConfigPath) {
	printf("MultipleKinects.initFromXml(xmlConfigPath)\n");

	XnStatus returnCode;

	printf("Initializing context ...\n");
	returnCode = this->context.Init();
	if(returnCode != XN_STATUS_OK) { THROW_XN_EXCEPTION("Initialisation failed!", returnCode); }
}

void MultipleKinects::waitForUpdate() {
	printf("MultipleKinects.waitForUpdate()\n");

}

void MultipleKinects::close() {
	printf("MultipleKinects.close()\n");

}
