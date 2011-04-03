#pragma once
#ifndef LOCKABLE_H_
#define LOCKABLE_H_

#include <pthread.h>

class Lockable {
public:
	Lockable(pthread_mutex_t);
	virtual ~Lockable();

	void lock();
	void unlock();

private:
	pthread_mutex_t mutex;

};

#endif // LOCKABLE_H_
