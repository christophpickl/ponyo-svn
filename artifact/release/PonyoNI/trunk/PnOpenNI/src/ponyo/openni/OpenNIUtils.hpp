#pragma once
#ifndef OPENNIUTILS_H_
#define OPENNIUTILS_H_

#include <XnLog.h>
#include <ponyo/openni/pnopenni_inc.hpp>

#define PN_SKELETON_JOINT_PLUS_ONE 25 /*+1, to avoid 0/1-index conversion*/

namespace pn {
class OpenNIUtils {
public:
	/**
	 * @param severity of type XnLogSeverity, eg: XN_LOG_VERBOSE
	 */
	static void enableXnLogging(const XnLogSeverity& severity);

	/**
	 * Checks if given file exists as well as logs additional errors if !XN_STATUS_OK.
	 */
	static XnStatus safeInitFromXml(xn::Context& context, const char* configXmlPath) throw (OpenNiException, FileNotFoundException);
	static bool fileExists(const char *filename);

	static const char* mapProductionNodeTypeToLabel(XnProductionNodeType type);
	static void dumpNodeInfos(xn::NodeInfoList& existingNodes) throw(OpenNiException);
	static void dumpNodeInfosByContext(xn::Context& context) throw(OpenNiException);
	static void dumpJointAvailability(xn::SkeletonCapability& skeleton) throw(OpenNiException);

	static const char* PN_PRODUCTION_NODE_NAMES[13/*+1, to avoid 0/1-index conversion*/];
	static const char* PN_SKELETON_JOINT[PN_SKELETON_JOINT_PLUS_ONE];
private:
	static Log* LOG;
	OpenNIUtils();
};

}

#endif // OPENNIUTILS_H_
