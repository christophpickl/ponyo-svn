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

	bool isMirrorModeEnabled() const;
	void setMirrorModeEnabled(bool);

	virtual const char* toCString() = 0; // TODO actually could be NULL assigned
	virtual /*MINOR const*/ std::string toString() = 0;

private:
	static Log* LOG;
	UserStateCallback userCallback;
	JointPositionCallback jointCallback;

	bool imageGeneratorEnabled;
	bool mirrorModeEnabled;
	// TODO SomeLogLevel OpenNilogConfiguration
};
}
#endif // GENERICCONFIG_HPP_
