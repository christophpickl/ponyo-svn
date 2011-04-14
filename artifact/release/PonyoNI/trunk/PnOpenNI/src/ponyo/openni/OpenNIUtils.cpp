#include <ponyo/openni/OpenNIUtils.hpp>
// TODO rename class to lower case i



namespace pn {

Log* OpenNIUtils::LOG = NEW_LOG();

// see XnProductionNodeType in XnTypes.h
const char* OpenNIUtils::PN_PRODUCTION_NODE_NAMES[13] = {
	NULL, "Device",
	"Depth Generator", "Image Generator", "Audio Generator", "IR Generator", "User Generator",
	"Recorder", "Player", "Gesture", "Scene", "Hands", "Codec"
};

// see XnSkeletonJoint in XnTypes.h
const char* OpenNIUtils::PN_SKELETON_JOINT[PN_SKELETON_JOINT_PLUS_ONE] = {
	NULL, "Head", "Neck", "Torso", "Waist",
	"Collar Left", "Shoulder Left", "Elbow Left", "Wrist Left", "Hand Left", "Fingertip Left",
	"Collar Right", "Shoulder Right", "Elbow Right", "Wrist Right", "Hand Right", "Fingertip Right",
	"Hip Left", "Knee Left", "Ankle Left", "Foot Left",
	"Hip Right", "Knee Right", "Ankle Right", "Foot Right"
};

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
	CHECK_XN(xnLogSetSeverityFilter(severity), "pn::enableXnLogging() while xnLogSetSeverityFilter()");
	CHECK_XN(xnLogSetMaskState("ALL", true), "pn::enableXnLogging() while xnLogSetMaskState()");
}

/*static*/ inline const char* OpenNIUtils::mapProductionNodeTypeToLabel(XnProductionNodeType type) {
	return OpenNIUtils::PN_PRODUCTION_NODE_NAMES[(int) type];
}

/*static*/ void OpenNIUtils::dumpNodeInfosByContext(xn::Context& context) throw(OpenNiException) {
	xn::NodeInfoList existingNodes;
	CHECK_XN(context.EnumerateExistingNodes(existingNodes), "Enumerating context nodes failed!");
	OpenNIUtils::dumpNodeInfos(existingNodes);
}

/*static*/ void OpenNIUtils::dumpNodeInfos(xn::NodeInfoList& existingNodes) throw(OpenNiException) {
//	printf("Existing Nodes DUMP:\n========================================\n");
	int i = 1;
	for(xn::NodeInfoList::Iterator it = existingNodes.Begin(); it != existingNodes.End(); it++, i++) {
		xn::NodeInfo info = *it;
		printf("%i. Node: %s\n", i, OpenNIUtils::mapProductionNodeTypeToLabel(info.GetDescription().Type));
		printf("\tInstance name: %s\n", info.GetInstanceName());
		printf("\tCreation: %s\n", info.GetCreationInfo());
		printf("\tType ID: %i\n", info.GetDescription().Type);
		printf("\tName: %s\n", info.GetDescription().strName);
		printf("\tVendor: %s\n", info.GetDescription().strVendor);
		XnVersion version = info.GetDescription().Version;
		printf("\tVersion: %i.%i.%i.%i\n", version.nMajor, version.nMinor, version.nMaintenance, version.nBuild);
		// LUXURY maybe print needed nodes as well...
		printf("\n");
	}
	printf("\n");
}

/*static*/ void OpenNIUtils::dumpJointAvailability(xn::SkeletonCapability& skeleton) throw(OpenNiException) {
	printf("\nAvailable Joints DUMP:\n========================================\n");
	for (int jointId = 1; jointId < PN_SKELETON_JOINT_PLUS_ONE; ++jointId) {
		const char* label = PN_SKELETON_JOINT[jointId];
		XnSkeletonJoint joinEnum = (XnSkeletonJoint) jointId;
		const char* isAvailable = BOOL2CHAR(skeleton.IsJointAvailable(joinEnum));
		const char* isActive = BOOL2CHAR(skeleton.IsJointActive(joinEnum, /*TODO seems as this is an ignored value?!*/TRUE));
		printf("%15s ... ID: %2i, available: %3s, active: %3s\n", label, jointId, isAvailable, isActive);
	}
	printf("\n");
}

}
