#pragma once
#ifndef PNOSC_HPP_
#define PNOSC_HPP_

#include <ponyo/PnOpenNI.hpp>

namespace pn {
class PnOSC {
public:
	PnOSC();
	virtual ~PnOSC();

	void startup();
private:
	static Log* LOG;
};
}
#endif // PNOSC_HPP_
