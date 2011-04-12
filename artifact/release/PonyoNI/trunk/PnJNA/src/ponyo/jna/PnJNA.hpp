#pragma once
#ifndef PNJNA_HPP_
#define PNJNA_HPP_

#include <ponyo/common/PnCommon.hpp>

namespace pn {

// typedef void(*UserStateChangedCallback)(unsigned int userId, unsigned int userState);

// typedef void(*JointPositionChangedCallback)(unsigned int userId, unsigned int jointId, float x, float y, float z);

/**
 * Initialize and start the OpenNI framework with the given configuration file.
 *
 * @param configPath path to an existing XML file to configure OpenNI with.
 */
extern "C" void startWithXml(const char* configPath);

/**
 * Stops any started generators, frees any memory, so a further reuse will not be possible anymore.
 */
extern "C" void destroy();

}

#endif // PNJNA_HPP_
