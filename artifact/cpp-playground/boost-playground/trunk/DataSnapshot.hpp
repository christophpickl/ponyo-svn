#pragma once
#ifndef DATASNAPSHOT_H_
#define DATASNAPSHOT_H_

class DataSnapshot {
public:
	DataSnapshot();
	virtual ~DataSnapshot();

	void lock();
	void unlock();

	int getNumber();
	void setNumber(int);

private:
	int number;
};

#endif /* DATASNAPSHOT_H_ */
