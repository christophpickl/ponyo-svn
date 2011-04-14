#pragma once
#ifndef STARTONICONFIG_HPP_
#define STARTONICONFIG_HPP_

#include <ponyo/openni/config/GenericConfig.hpp>

namespace pn {
class StartOniConfig : public GenericConfig {
public:
	StartOniConfig(
		const char* oniRecordingPath,
		UserStateCallback userCallback,
		JointPositionCallback jointCallback
	);
	virtual ~StartOniConfig();

	const char* getOniRecordingPath() const;

	friend std::ostream& operator<<(std::ostream& ostream, const StartOniConfig& tthis) {
		return ostream << "StartOniConfig[oniRecordingPath=" << tthis.oniRecordingPath << "]";
	}
	const char* toCString();
	std::string toString();

private:
	static Log* LOG;
	const char* oniRecordingPath;
};
}

#endif // STARTONICONFIG_HPP_
