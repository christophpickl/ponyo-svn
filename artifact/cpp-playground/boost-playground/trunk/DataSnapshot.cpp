#include <stdio.h>
#include <pthread.h>
#include "DataSnapshot.hpp"

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

DataSnapshot::DataSnapshot() : number(0) {
	printf("new DataSnapshot()\n");
}

DataSnapshot::~DataSnapshot() {
	printf("~DataSnapshot()\n");
}

void DataSnapshot::lock() {
	printf("DataSnapshot.lock()\n");
	pthread_mutex_lock(&mutex);
}

void DataSnapshot::unlock() {
	printf("DataSnapshot.unlock()\n");
	pthread_mutex_unlock(&mutex);
}

int DataSnapshot::getNumber() {
	printf("DataSnapshot.getNumber() ... this.number=%i\n", this->number);
	return this->number;
}

void DataSnapshot::setNumber(int value) {
	printf("DataSnapshot.setNumber(value=%i) ... this.number=%i\n", value, this->number);
	this->number = value;
}
