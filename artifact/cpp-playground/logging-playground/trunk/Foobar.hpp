#pragma once
#ifndef FOOBAR_H_
#define FOOBAR_H_

#include "Log.hpp"

class Foobar {
public:
	Foobar();
	virtual ~Foobar();

	void doit();

private:
	static Log* LOG;
};

#endif // FOOBAR_H_
