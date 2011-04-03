#pragma once
#ifndef ASYNC_H_
#define ASYNC_H_

#include <stdio.h>
#include <vector>

template <class T>
class Async {

public:

	Async() {
		printf("new Async()\n");
	}

	~Async() {
		printf("~Async()\n");

//		delete this->listeners;
	}

	void add(T &listener) {
		printf("Async.add(listener)\n");
		this->listeners.push_back(listener);
	}

protected:

	std::vector<T> listeners;

};

#endif /* ASYNC_H_ */
