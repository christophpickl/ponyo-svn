/*
 * @version 0.1
 */
#pragma once
#ifndef PNCOMMON_INC_HPP_
#define PNCOMMON_INC_HPP_

#include <stdio.h>
#include <iostream>
#include <string>
#include <vector>

#define AT __FILE__, __LINE__

// useful when dealing with pseudo boolean values
#define YES "Yes"
#define NO "No"
#define BOOL2CHAR(b) (b == 1) ? YES:  NO;

namespace pn {
	inline const char * const boolToString(bool value) { return value ? "true" : "false"; }
}

#endif // PNCOMMON_INC_HPP_
