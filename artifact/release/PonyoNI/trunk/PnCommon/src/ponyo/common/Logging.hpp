/**
 * Include merge header, as well defines NEW_LOG() macro.
 */
#pragma once
#ifndef LOGGING_HPP_
#define LOGGING_HPP_

#include <ponyo/common/logging/Log.hpp>
#include <ponyo/common/logging/LogFactory.hpp>

/**
 * Sample usage:
 *
 * Log* MyClass::LOG = NEW_LOG();
 *
 * Assumed you have defined a static member:
 *
 * private: static Log* LOG;
 */
#define NEW_LOG() LogFactory::getLog(__FILE__);

#endif // LOGGING_HPP_
