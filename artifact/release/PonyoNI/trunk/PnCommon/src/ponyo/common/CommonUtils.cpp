#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include <ponyo/common/CommonUtils.hpp>
//#include <XnCppWrapper.h>

namespace pn {
Log* CommonUtils::LOG = NEW_LOG();

/*private*/ CommonUtils::CommonUtils() { }
/*private*/ CommonUtils::~CommonUtils() { }

/*static*/ void CommonUtils::waitHitEnter(bool printDefaultPrompt) {
	if(printDefaultPrompt == true) {
		printf("\n  ======> Hit ENTER to continue ...\n\n");
	}
//	xn::Context context;
	std::string input;
	std::getline(std::cin, input);
}

/*static*/ void CommonUtils::sleep(int seconds) {
	LOG->debug("sleep(seconds)");

	boost::posix_time::seconds workTime(seconds);
	boost::this_thread::sleep(workTime);
}

}
