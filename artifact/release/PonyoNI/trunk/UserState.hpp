/**
 * Enum class with some enhancements for iterating and toString conversion.
 *
 * Usage:
 *   printf("UserState.LOST: %s\n", UserState::toString((UserState::Enum) UserState::ID_LOST));
 *   for(UserState::Enum s = UserState::BEGIN; s < UserState::END; ++s) {
 *     printf("state: %i\n", s);
 *   }
 */
#pragma once
#ifndef USERSTATE_HPP_
#define USERSTATE_HPP_

#define STRINGIFY( name ) # name

namespace pn {
namespace UserState {

typedef const int StateId;
StateId ID_NEW                              = 0;
StateId ID_CALIBRATION_STARTED              = 1;
StateId ID_CALIBRATION_ENDED_SUCCESSFULLY   = 2;
StateId ID_CALIBRATION_ENDED_UNSUCCESSFULLY = 3;
StateId ID_LOST                             = 4;

enum Enum {
	NEW                                = ID_NEW, BEGIN = NEW,
	CALIBRATION_STARTED                = ID_CALIBRATION_STARTED,
	CALIBRATION_ENDED_SUCCESSFULLY     = ID_CALIBRATION_ENDED_SUCCESSFULLY,
	CALIBRATION_ENDED_UNSUCCESSFULLY   = ID_CALIBRATION_ENDED_UNSUCCESSFULLY,
	LOST                               = ID_LOST,
	END, ERROR = 0xFF
};

const char* STATE_TO_STRING[] = {
	STRINGIFY( NEW ),
	STRINGIFY( CALIBRATION_STARTED ),
	STRINGIFY( CALIBRATION_ENDED_SUCCESSFULLY ),
	STRINGIFY( CALIBRATION_ENDED_UNSUCCESSFULLY ),
	STRINGIFY( LOST )
};

const Enum& operator++(Enum& other) {
	return (other = (Enum) (other + 1));
}
const char* toString(Enum state) {
	return STATE_TO_STRING[state];
}

}
}

#endif // USERSTATE_HPP_
