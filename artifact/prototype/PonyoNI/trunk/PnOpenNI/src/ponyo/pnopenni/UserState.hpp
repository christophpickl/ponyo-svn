#pragma once
#ifndef USERSTATE_HPP_
#define USERSTATE_HPP_

namespace pn {

// TODO typedef enum UserState {
//	USER_STATE_NEW           = 1,
//	USER_STATE_POSE_DETECTED = 2
//} UserState;

typedef unsigned int UserState;

/** When enters the scene. */
const UserState USER_STATE_NEW                             = 100;

/** ??? */
const UserState USER_STATE_POSE_DETECTED                   = 200;

/** Started standing in Psi, waiting. */
const UserState USER_STATE_CALIBRATION_STARTED             = 300;

/** Waiting in Psi success, start tracking. */
const UserState USER_STATE_CALIBRATION_ENDED_SUCCESFULLY   = 410;

/** Waiting in Psi fail, re-requesting calibraiton. */
const UserState USER_STATE_CALIBRATION_ENDED_UNSUCCESFULLY = 420;

/** Timeout occured; user ID will be freed. */
const UserState USER_STATE_LOST                            = 999;

}

#endif // USERSTATE_HPP_
