#include <ponyo/openni/config/GenericConfig.hpp>

namespace pn {

Log* GenericConfig::LOG = NEW_LOG();

GenericConfig::GenericConfig(
		UserStateCallback userCallback,
		JointPositionCallback jointCallback) :
			userCallback(userCallback),
			jointCallback(jointCallback),
			imageGeneratorEnabled(false) // disabled by default
{
	LOG->debug("new GenericConfig(..)");
}

GenericConfig::~GenericConfig() {
	LOG->debug("~GenericConfig()");
}

UserStateCallback GenericConfig::getUserCallback() const { return this->userCallback; }

JointPositionCallback GenericConfig::getJointCallback() const { return this->jointCallback; }

bool GenericConfig::isImageGeneratorEnabled() const { return this->imageGeneratorEnabled; }
void GenericConfig::setImageGeneratorEnabled(bool value) { this->imageGeneratorEnabled = value; }

bool GenericConfig::isMirrorModeEnabled() const { return this->mirrorModeEnabled; }
void GenericConfig::setMirrorModeEnabled(bool value) { this->mirrorModeEnabled = value; }

}
