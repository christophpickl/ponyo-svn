#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include <ponyo/common/CommonUtils.hpp>

namespace pn {
Log* CommonUtils::LOG = NEW_LOG();

CommonUtils::CommonUtils() { }
CommonUtils::~CommonUtils() { }

/*static*/ void CommonUtils::sleep(int seconds) {
	LOG->debug("sleep(seconds)");

	boost::posix_time::seconds workTime(seconds);
	boost::this_thread::sleep(workTime);
}

}
