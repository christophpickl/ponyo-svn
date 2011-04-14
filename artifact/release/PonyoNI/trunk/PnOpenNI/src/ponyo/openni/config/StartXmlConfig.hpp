#pragma once
#ifndef STARTXMLCONFIG_HPP_
#define STARTXMLCONFIG_HPP_

#include <ponyo/PnCommon.hpp>
#include <ponyo/openni/config/GenericConfig.hpp>

namespace pn {
class StartXmlConfig : public GenericConfig {
public:
	StartXmlConfig(
		const char* xmlConfigPath,
		UserStateCallback userCallback,
		JointPositionCallback jointCallback
	);
	virtual ~StartXmlConfig();

	const char* getXmlConfigPath() const;

	friend std::ostream& operator<<(std::ostream& ostream, const StartXmlConfig& tthis) {
		return ostream << "StartXmlConfig[xmlConfigPath=" << tthis.xmlConfigPath << "]";
	}
	const char* toCString();
	std::string toString();

private:
	static Log* LOG;
	const char* xmlConfigPath;
};
}

#endif // STARTXMLCONFIG_HPP_
