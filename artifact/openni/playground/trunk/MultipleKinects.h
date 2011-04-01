
#ifndef MULTIPLEKINECTS_H_
#define MULTIPLEKINECTS_H_

#include <string>
#include <XnCppWrapper.h>

class MultipleKinects {
public:
	MultipleKinects();
	virtual ~MultipleKinects();

	void initFromXml(std::string);
	void waitForUpdate();
	void close();

private:
	xn::Context context;
};

#endif /* MULTIPLEKINECTS_H_ */
