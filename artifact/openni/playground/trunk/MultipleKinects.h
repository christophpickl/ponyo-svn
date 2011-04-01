
#ifndef MULTIPLEKINECTS_H_
#define MULTIPLEKINECTS_H_

#include <string>
#include <vector.h>
#include <XnCppWrapper.h>
#include "NiDevice.h"

class MultipleKinects {
public:
	MultipleKinects();
	virtual ~MultipleKinects();

	void initFromXml(std::string);
	void waitForUpdate();
	void close();

private:
	xn::Context context;
	vector<NiDevice*> devices;
};

#endif /* MULTIPLEKINECTS_H_ */
