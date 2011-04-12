#pragma once
#ifndef PNCONTEXT_HPP_
#define PNCONTEXT_HPP_

#include <ponyo/openni/pnopenni_inc.hpp>

namespace pn {
class PnContext {

public:
	PnContext();
	virtual ~PnContext();

	void startWithXml(const char* configPath) throw(OpenNiException);
	void startRecording(const char* oniFilePath) throw(OpenNiException);
	void destroy();

private:
	static Log* LOG;

	xn::Context context;
	xn::DepthGenerator depthGenerator;

};
}
#endif // PNCONTEXT_HPP_
