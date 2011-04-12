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

	CHECK_XN(xnLogInitSystem(), "pn::enableXnLogging() while xnLogInitSystem()");
	CHECK_XN(xnLogSetLineInfo(true), "pn::enableXnLogging() while xnLogSetLineInfo()");
	CHECK_XN(xnLogSetConsoleOutput(true), "pn::enableXnLogging() while xnLogSetConsoleOutput()");
	CHECK_XN(xnLogSetFileOutput(false), "pn::enableXnLogging() while xnLogSetFileOutput()");
//	CHECK_XN(xnLogSetSeverityFilter(/*XnLogSeverity*/XN_LOG_VERBOSE), "pn::enableXnLogging() while xnLogSetSeverityFilter()");
	CHECK_XN(xnLogSetSeverityFilter(severity), "pn::enableXnLogging() while xnLogSetSeverityFilter()");
	CHECK_XN(xnLogSetMaskState("ALL", true), "pn::enableXnLogging() while xnLogSetMaskState()");
}

}
