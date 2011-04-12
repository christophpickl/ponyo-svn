#include <ponyo/openni/PnContext.hpp>

namespace pn {

Log* PnContext::LOG = NEW_LOG();

PnContext::PnContext() {
	LOG->debug("new PnContext()");
}

void PnContext::startRecording(const char* oniFilePath) throw(OpenNiException) {
	LOG->info("startRecording(oniFilePath)");

	LOG->debug("Initializing context ...");
	XNTRY(this->context.Init(), "Could not initialize OpenNI context!");

	LOG->debug("Opening *.oni file recording ...");
	XNTRY(this->context.OpenFileRecording(oniFilePath), "Could not open *.oni file!");

	LOG->debug("Creating depth generator ...");
	XNTRY(this->depthGenerator.Create(this->context), "Could not create depth generator!");
}
void PnContext::startWithXml(const char* configPath) throw(OpenNiException) {
	LOG->info("startWithXml(configPath)");

	LOG->debug("Initializing context ...");
	XNTRY(this->context.InitFromXmlFile(configPath), "Could not initialize OpenNI context from XML!");

	// TODO get xnErrors working + OpenNiException + string copy (as it seems as something get unintentionally freed)
//	xn::EnumerationErrors xnInitErrors;
//	XnStatus initXnStatus = this->context.InitFromXmlFile(configPath, &xnInitErrors);
//	if (initXnStatus != XN_STATUS_OK) {
//		if (initXnStatus == XN_STATUS_NO_NODE_PRESENT) {
//			char xnErrorMessages[1024];
//			xnInitErrors.ToString(xnErrorMessages, 1024);
//			fprintf(stderr, "OpenNI errors: %s\n", xnErrorMessages);
//		}
//		THROW_XN_EXCEPTION(initXnStatus, "Could not initialize OpenNI context from XML!");
//	}

	LOG->debug("Creating depth generator ...");
	XNTRY(this->depthGenerator.Create(this->context), "Could not create depth generator!");
}

void PnContext::destroy() {
	LOG->info("destory()");

	LOG->debug("Shutting down OpenNI context...");
	this->context.Shutdown();
}

PnContext::~PnContext() {
	LOG->debug("~PnContext()");
}

}
