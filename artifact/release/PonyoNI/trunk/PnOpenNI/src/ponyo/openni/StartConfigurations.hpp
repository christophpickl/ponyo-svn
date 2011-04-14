#pragma once
#ifndef STARTCONFIGURATIONS_HPP_
#define STARTCONFIGURATIONS_HPP_

#include <string>
#include <sstream>
//#include <iostream>
#include <ponyo/openni/pnopenni_inc.hpp>

namespace pn {
class AbstractConfiguration {
public:
	AbstractConfiguration(UserStateCallback userCallback,
		JointPositionCallback jointCallback, bool imageGeneratorEnabled = false) :
			userCallback(userCallback),
			jointCallback(jointCallback),
			imageGeneratorEnabled(imageGeneratorEnabled)
		{
			// nothing to do
		}

	UserStateCallback getUserCallback() const {
		return this->userCallback;
	}

	JointPositionCallback getJointCallback() const {
		return this->jointCallback;
	}

	bool isImageGeneratorEnabled() const {
		return this->imageGeneratorEnabled;
	}
	void setMirrorModeEnabled(bool mirrorModeEnabled) {
		this->mirrorModeEnabled = mirrorModeEnabled;
	}
	bool isMirrorModeEnabled() const {
		return this->mirrorModeEnabled;
	}

	virtual const char* toCString() = 0; // TODO actually could be NULL assigned
	virtual /*MINOR const*/ std::string toString() = 0;

private:
	UserStateCallback userCallback;
	JointPositionCallback jointCallback;

	bool imageGeneratorEnabled;
	bool mirrorModeEnabled;
};

class StartXmlConfiguration : public AbstractConfiguration {
public:
	StartXmlConfiguration(
		const char* xmlConfigPath,
		UserStateCallback userCallback,
		JointPositionCallback jointCallback,
		bool imageGeneratorEnabled = false) :
			AbstractConfiguration(userCallback, jointCallback, imageGeneratorEnabled),
			xmlConfigPath(xmlConfigPath)
	{
		// nothing to do
	}

	const char* getXmlConfigPath() const {
		return this->xmlConfigPath;
	}

	friend std::ostream& operator<<(std::ostream& ostream, const StartXmlConfiguration& tthis) {
		return ostream << "StartXmlConfiguration[xmlConfigPath=" << tthis.xmlConfigPath << "]";
	}
	const char* toCString() {
		return this->toString().c_str();
	}
	std::string toString() {
		std::stringstream ss;
		ss << *this;
		return ss.str();
	}

private:
	const char* xmlConfigPath;
};

class StartOniConfiguration : public AbstractConfiguration {
public:
	StartOniConfiguration(
		const char* oniRecordingPath,
		UserStateCallback userCallback,
		JointPositionCallback jointCallback,
		bool imageGeneratorEnabled = false) :
			AbstractConfiguration(userCallback, jointCallback, imageGeneratorEnabled),
			oniRecordingPath(oniRecordingPath)
	{
		// nothing to do
	}

	const char* getOniRecordingPath() const {
		return this->oniRecordingPath;
	}

	friend std::ostream& operator<<(std::ostream& ostream, const StartOniConfiguration& tthis) {
		return ostream << "StartOniConfiguration[oniRecordingPath=" << tthis.oniRecordingPath << "]";
	}
	const char* toCString() {
		return this->toString().c_str();
	}
	std::string toString() {
		std::stringstream ss;
		ss << *this;
		return ss.str();
	}

private:
	const char* oniRecordingPath;
};

}

#endif // STARTCONFIGURATIONS_HPP_
