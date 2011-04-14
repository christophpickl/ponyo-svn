#include <ponyo/openni/config/StartXmlConfig.hpp>

namespace pn {

Log* StartXmlConfig::LOG = NEW_LOG();

StartXmlConfig::StartXmlConfig(
		const char* xmlConfigPath,
		UserStateCallback userCallback,
		JointPositionCallback jointCallback) :
			GenericConfig(userCallback, jointCallback),
			xmlConfigPath(xmlConfigPath)
{
	LOG->debug("new StartXmlConfig(..)");
}

StartXmlConfig::~StartXmlConfig() {
	LOG->debug("~StartXmlConfig()");
}

const char* StartXmlConfig::getXmlConfigPath() const {
	return this->xmlConfigPath;
}

const char* StartXmlConfig::toCString() {
	return this->toString().c_str();
}
std::string StartXmlConfig::toString() {
	std::stringstream ss;
	ss << *this;
	return ss.str();
}

}
