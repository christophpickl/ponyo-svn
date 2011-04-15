#pragma once
#ifndef GENERICCONFIG_HPP_
#define GENERICCONFIG_HPP_

#include <string>
#include <sstream>
#include <ponyo/openni/pnopenni_inc.hpp>

namespace pn {
class GenericConfig {
public:
	GenericConfig(
		UserStateCallback userCallback,
		JointPositionCallback jointCallback
	);
	virtual ~GenericConfig();

	UserStateCallback getUserCallback() const;
	JointPositionCallback getJointCallback() const;

	bool isImageGeneratorEnabled() const;
	void setImageGeneratorEnabled(bool);

	bool isDepthGeneratorEnabled() const;
	void setDepthGeneratorEnabled(bool);

	/*TODO const?!*/ bool isUserGeneratorEnabled() const;
	void setUserGeneratorEnabled(bool);

	bool isMirrorModeEnabled() const;
	void setMirrorModeEnabled(bool);

	virtual const char* toCString() = 0; // TODO actually could be NULL assigned
	virtual /*MINOR const*/ std::string toString() = 0;

private:
	static Log* LOG;

	// TODO SomeLogLevel OpenNilogConfiguration
	bool mirrorModeEnabled;

	bool imageGeneratorEnabled;
		// bool mirrorModeEnabled

	bool depthGeneratorEnabled;
		// bool mirrorModeEnabled

	bool userGeneratorEnabled;// TODO if userGen enabled, depthGen HAS to be enabled as well (validate config!)
		UserStateCallback userCallback;
		JointPositionCallback jointCallback;
		// TODO float smoothing;
		// confidence?
		// SomeSkeletoProfile toActivateOrDeactivateTrackingOfCertainJoints; ... using: skeletonCapability.SetJointActive(x)


};
}
#endif // GENERICCONFIG_HPP_
