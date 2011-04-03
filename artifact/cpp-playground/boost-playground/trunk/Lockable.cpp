#include <stdio.h>
#include "Lockable.hpp"

Lockable::Lockable(pthread_mutex_t pMutex) : mutex(pMutex) {
	printf("new Lockable(mutex)\n");
}

Lockable::~Lockable() {
	printf("~Lockable()\n");
}

void Lockable::lock() {
	printf("Lockable.lock()\n");
	pthread_mutex_lock(&this->mutex);
}

void Lockable::unlock() {
	printf("Lockable.unlock()\n");
	pthread_mutex_unlock(&this->mutex);
}
