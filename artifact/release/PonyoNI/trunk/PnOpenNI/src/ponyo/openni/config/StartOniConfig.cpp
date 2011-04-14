#include <ponyo/openni/config/StartOniConfig.hpp>

namespace pn {

Log* StartOniConfig::LOG = NEW_LOG();

StartOniConfig::StartOniConfig(
		const char* oniRecordingPath,
		UserStateCallback userCallback,
		JointPositionCallback jointCallback) :
		GenericConfig(userCallback, jointCallback),
			oniRecordingPath(oniRecordingPath)
{
	LOG->debug("new StartOniConfig(..)");
}

StartOniConfig::~StartOniConfig() {
	LOG->debug("~StartOniConfig()");

}

const char* StartOniConfig::getOniRecordingPath() const {
	return this->oniRecordingPath;
}

const char* StartOniConfig::toCString() {
	return this->toString().c_str();
}
std::string StartOniConfig::toString() {
	std::stringstream ss;
	ss << *this;
	return ss.str();
}
}
