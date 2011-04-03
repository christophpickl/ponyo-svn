#include <stdio.h>
#include "DataSnapshot.hpp"

DataSnapshot::DataSnapshot(pthread_mutex_t pMutex) : Lockable(pMutex), number(0) {
	printf("new DataSnapshot(mutex)\n");
}

DataSnapshot::~DataSnapshot() {
	printf("~DataSnapshot()\n");
}

int DataSnapshot::getNumber() {
	printf("DataSnapshot.getNumber() ... this.number=%i\n", this->number);
	return this->number;
}

void DataSnapshot::setNumber(int value) {
	printf("DataSnapshot.setNumber(value=%i) ... this.number=%i\n", value, this->number);
	this->number = value;
}
