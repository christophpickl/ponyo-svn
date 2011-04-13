#pragma once
#ifndef PNJNA_HPP_
#define PNJNA_HPP_

#include <ponyo/openni/PnOpenNI.hpp>

/**
 * Initialize and start the OpenNI framework with the given configuration file.
 *
 * @param configPath path to an existing XML file to configure OpenNI with.
 * @param userStateCallback
 * @param jointPositionCallback
 */
extern "C" void pnStartWithXml(
		const char* configPath,
		pn::UserStateCallback userStateCallback,
		pn::JointPositionCallback jointPositionCallback
	);

/**
 * Initialize and start the OpenNI framework using a prerecorded session.
 *
 * @param oniFilePath path to an existing ONI file which should be replayed.
 * @param userStateCallback
 * @param jointPositionCallback
 */
extern "C" void pnStartRecording(
		const char* oniFilePath,
		pn::UserStateCallback userStateCallback,
		pn::JointPositionCallback jointPositionCallback
	);
/**
 * Stops any started generators, frees any memory, so a further reuse will not be possible anymore.
 */
extern "C" void pnDestroy();

/*private*/ void __pnStart(
		const char* configOrOniFile,
		bool isConfigFlag,
		pn::UserStateCallback userStateCallback,
		pn::JointPositionCallback jointPositionCallback
	);

#endif // PNJNA_HPP_
