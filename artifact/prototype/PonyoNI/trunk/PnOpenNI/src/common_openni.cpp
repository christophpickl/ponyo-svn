#include "common_openni.hpp"
#include "XnLog.h"

namespace pn {
void initOpenniLogging() {
	CHECK_RC(xnLogInitSystem(), "xnLogInitSystem");
	CHECK_RC(xnLogSetLineInfo(true), "xnLogSetLineInfo");
	CHECK_RC(xnLogSetConsoleOutput(true), "xnLogSetConsoleOutput");
	CHECK_RC(xnLogSetFileOutput(false), "xnLogSetFileOutput");
	CHECK_RC(xnLogSetSeverityFilter(/*XnLogSeverity*/XN_LOG_VERBOSE), "xnLogSetSeverityFilter");
	CHECK_RC(xnLogSetMaskState("ALL", true), "xnLogSetMaskState");
}
}
