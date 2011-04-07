#pragma once
#ifndef CAMINITIALIZERLISTENER_HPP_
#define CAMINITIALIZERLISTENER_HPP_

#include <vector>
#include "Cam.hpp"

namespace pn {
class CamInitializerListener {

public:
	virtual void onInitializedCams(std::vector<Cam*>) = 0;
	virtual void onException(Exception&) = 0;

};
}

#endif // CAMINITIALIZERLISTENER_HPP_
