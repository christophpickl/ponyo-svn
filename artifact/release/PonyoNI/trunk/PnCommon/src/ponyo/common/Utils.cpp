#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include <ponyo/common/Utils.hpp>

namespace pn {
Log* Utils::LOG = NEW_LOG();

Utils::Utils() { }
Utils::~Utils() { }

/*static*/ void Utils::sleep(int seconds) {
	LOG->debug("sleep(seconds)");
	boost::posix_time::seconds workTime(seconds);
	boost::this_thread::sleep(workTime);
}

}
