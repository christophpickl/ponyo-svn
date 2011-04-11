#pragma once
#ifndef LOGGING_HPP_
#define LOGGING_HPP_

#include <ponyo/pncommon/log/Log.hpp>
#include <ponyo/pncommon/log/LogFactory.hpp>

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
