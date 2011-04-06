#include "OpenNiManager.hpp"
#include "log/LogFactory.hpp"

namespace pn {

Log* OpenNiManager::LOG = NEW_LOG(__FILE__)

OpenNiManager::OpenNiManager() { //  throw (ConnectionException) {
	LOG->debug("new OpenNiManager()");
}

OpenNiManager::~OpenNiManager() {
	LOG->debug("~OpenNiManager()");
}

}
