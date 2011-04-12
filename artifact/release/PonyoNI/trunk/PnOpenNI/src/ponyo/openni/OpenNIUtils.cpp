#include <ponyo/openni/OpenNIUtils.hpp>

namespace pn {

Log* OpenNIUtils::LOG = NEW_LOG();

OpenNIUtils::OpenNIUtils() {
	LOG->debug("new OpenNIUtils()");
}

OpenNIUtils::~OpenNIUtils() {
	LOG->debug("~OpenNIUtils()");
}


/*static*/ void OpenNIUtils::enableXnLogging(const XnLogSeverity& severity) {
	LOG->info("enableXnLogging(severity)");

	XNTRY(xnLogInitSystem(), "pn::enableXnLogging() while xnLogInitSystem()");
	XNTRY(xnLogSetLineInfo(true), "pn::enableXnLogging() while xnLogSetLineInfo()");
	XNTRY(xnLogSetConsoleOutput(true), "pn::enableXnLogging() while xnLogSetConsoleOutput()");
	XNTRY(xnLogSetFileOutput(false), "pn::enableXnLogging() while xnLogSetFileOutput()");
//	XNTRY(xnLogSetSeverityFilter(/*XnLogSeverity*/XN_LOG_VERBOSE), "pn::enableXnLogging() while xnLogSetSeverityFilter()");
	XNTRY(xnLogSetSeverityFilter(severity), "pn::enableXnLogging() while xnLogSetSeverityFilter()");
	XNTRY(xnLogSetMaskState("ALL", true), "pn::enableXnLogging() while xnLogSetMaskState()");
}

}
