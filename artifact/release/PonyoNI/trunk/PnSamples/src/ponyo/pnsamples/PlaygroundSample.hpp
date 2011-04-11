#pragma once
#ifndef PLAYGROUNDSAMPLE_HPP_
#define PLAYGROUNDSAMPLE_HPP_

#include <ponyo/pncommon/Logging.hpp>

namespace pn {
class PlaygroundSample {
public:
	PlaygroundSample();
	virtual ~PlaygroundSample();
private:
	static Log* LOG;
};
}

#endif // PLAYGROUNDSAMPLE_HPP_
