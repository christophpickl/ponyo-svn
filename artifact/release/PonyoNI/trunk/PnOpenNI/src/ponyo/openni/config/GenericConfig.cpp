#include <ponyo/openni/config/GenericConfig.hpp>

namespace pn {

Log* GenericConfig::LOG = NEW_LOG();

GenericConfig::GenericConfig(
		UserStateCallback userCallback,
		JointPositionCallback jointCallback) :
			userCallback(userCallback),
			jointCallback(jointCallback),
			imageGeneratorEnabled(false), // disabled by default
			depthGeneratorEnabled(true),
			userGeneratorEnabled(true)
{
	LOG->debug("new GenericConfig(..)");
}

GenericConfig::~GenericConfig() {
	LOG->debug("~GenericConfig()");
}

UserStateCallback GenericConfig::getUserCallback() const { return this->userCallback; }
JointPositionCallback GenericConfig::getJointCallback() const { return this->jointCallback; }

AsyncExceptionCallback GenericConfig::getAsyncExceptionCallback() const { return this->asyncExceptionCallback; }
void GenericConfig::setAsyncExceptionCallback(AsyncExceptionCallback value) { this->asyncExceptionCallback = value; }

bool GenericConfig::isImageGeneratorEnabled() const { return this->imageGeneratorEnabled; }
void GenericConfig::setImageGeneratorEnabled(bool value) { this->imageGeneratorEnabled = value; }

bool GenericConfig::isDepthGeneratorEnabled() const { return this->depthGeneratorEnabled; }
void GenericConfig::setDepthGeneratorEnabled(bool value) { this->depthGeneratorEnabled = value; }

bool GenericConfig::isUserGeneratorEnabled() const { return this->userGeneratorEnabled; }
void GenericConfig::setUserGeneratorEnabled(bool value) { this->userGeneratorEnabled = value; }

bool GenericConfig::isMirrorModeEnabled() const { return this->mirrorModeEnabled; }
void GenericConfig::setMirrorModeEnabled(bool value) { this->mirrorModeEnabled = value; }

}
