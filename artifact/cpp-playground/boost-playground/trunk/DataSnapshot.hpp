#pragma once
#ifndef DATASNAPSHOT_H_
#define DATASNAPSHOT_H_

#include "Lockable.hpp"

class DataSnapshot : public Lockable {
public:
	DataSnapshot(pthread_mutex_t);
	virtual ~DataSnapshot();

	int getNumber();
	void setNumber(int);

private:
	int number;
};

#endif /* DATASNAPSHOT_H_ */
