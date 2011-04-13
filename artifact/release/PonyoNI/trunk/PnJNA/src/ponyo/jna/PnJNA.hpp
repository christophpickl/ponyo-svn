#pragma once
#ifndef PNJNA_HPP_
#define PNJNA_HPP_

#include <ponyo/PnOpenNI.hpp>

/**
 * Initialize and start the OpenNI framework with the given configuration file.
 */
extern "C" int pnStartByXmlConfig(
		const char* configPath,
		pn::UserStateCallback userStateCallback,
		pn::JointPositionCallback jointPositionCallback
	);

/**
 * Initialize and start the OpenNI framework using a prerecorded session.
 */
extern "C" int pnStartByOniRecording(
		const char* oniFilePath,
		pn::UserStateCallback userStateCallback,
		pn::JointPositionCallback jointPositionCallback
	);

/**
 * Stops any started generators, frees any memory.
 *
 * Theoretically it should be possible to call start again ;)
 */
extern "C" void pnDestroy();

/**
 * Internal method containing common generic startup logic.
 */
/*private*/ int __pnStart(
		const char* configOrOniFile,
		bool isConfigFlag,
		pn::UserStateCallback userStateCallback,
		pn::JointPositionCallback jointPositionCallback
	);

#endif // PNJNA_HPP_
