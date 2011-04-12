#pragma once
#ifndef OPENNIUTILS_H_
#define OPENNIUTILS_H_

#include <ponyo/openni/pnopenni_inc.hpp>

namespace pn {
class OpenNIUtils {
public:
	OpenNIUtils();
	virtual ~OpenNIUtils();
	static void enableXnLogging(const XnLogSeverity&);
private:
	static Log* LOG;
};
}

#endif // OPENNIUTILS_H_
