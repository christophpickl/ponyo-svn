#pragma once
#ifndef PNJNA_HPP_
#define PNJNA_HPP_


typedef void(*UserStateChangedCallback)(unsigned int, unsigned int);
typedef void(*JointPositionChangedCallback)(unsigned int, unsigned int, float, float, float);

extern "C" void startup(UserStateChangedCallback, JointPositionChangedCallback);
extern "C" void destroy();

#endif // PNJNA_HPP_
