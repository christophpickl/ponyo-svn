#pragma once
#ifndef PNJNA_HPP_
#define PNJNA_HPP_


typedef void(*foobarCallback)(int);

extern "C" void addCallback(foobarCallback);

extern "C" int pnGetNumber();

#endif // PNJNA_HPP_
