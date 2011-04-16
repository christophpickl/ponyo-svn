#pragma once
#ifndef USERSTATE_HPP_
#define USERSTATE_HPP_

namespace pn {

typedef int UserState;

const UserState USER_STATE_NEW                             = 0;
const UserState USER_STATE_CALIBRATION_STARTED             = 1;
const UserState USER_STATE_CALIBRATION_ENDED_UNSUCCESFULLY = 2;
const UserState USER_STATE_CALIBRATION_ENDED_SUCCESFULLY   = 3; // TODO rename to IS_TRACKING
const UserState USER_STATE_LOST                            = 4;


// TODO add more user states
//const UserState USER_STATE_POSE_DETECTED                   = x;
// const UserState USER_STATE_TRACKING_STARTED == USER_STATE_CALIBRATION_ENDED_UNSUCCESFULLY = 500;
// const UserState USER_STATE_LOST_PROBABLY_STARTED        = 910; // ... premature timeout, as openni timeout is too long
// const UserState USER_STATE_LOST_PROBABLY_ENDED          = 920; // ... after user was invisible for a while, we received updated joints

}

#endif // USERSTATE_HPP_
