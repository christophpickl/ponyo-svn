#pragma once
#ifndef OPENNIMANAGER_HPP_
#define OPENNIMANAGER_HPP_

#include "log/Log.hpp"
#include "OpenNiException.hpp"

namespace pn {
class OpenNiManager {
public:
	OpenNiManager();
	virtual ~OpenNiManager();

	void init() throw (OpenNiException);;

private:
	static Log* LOG;
};
}

#endif // OPENNIMANAGER_HPP_
