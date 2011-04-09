#pragma once
#ifndef ASYNC_H_
#define ASYNC_H_

#include <stdio.h>
#include <vector>

#include <ponyo/pncommon/log/LogFactory.hpp>

namespace pn {
//Log* Async::LOG = NEW_LOG(__FILE__)

template <class T>
class Async {

public:
	Async() {
//		LOG_ASYNC->debug("new Async()\n");
	}
	~Async() {
//		LOG_ASYNC->debug("~Async()\n");

//		delete this->listeners; only delete vector (datastructure), not listeners themselves!
	}

	void addListener(const T& listener) {
//		LOG_ASYNC->debug("addListener(listener)\n");
		this->listeners.push_back(listener);
	}

	void removeListener(const T& listener) {
		// TODO implement me!
	}

protected:
	std::vector<T> listeners;

//private:
//	static Log LOG;

};
}

#endif /* ASYNC_H_ */
