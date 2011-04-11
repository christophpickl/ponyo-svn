
#ifndef MULTIPLEKINECTS_H_
#define MULTIPLEKINECTS_H_

#include <vector.h>
#include <XnCppWrapper.h>
#include "NiDevice.h"

class MultipleKinects {
public:
	MultipleKinects();
	virtual ~MultipleKinects();

	void init();
	void start();
	void waitForUpdate();
	void close();

private:
	xn::Context context;
	vector<NiDevice*> devices;
	ImageSaver* imageSaver; // TODO remove afterwards
};

#endif /* MULTIPLEKINECTS_H_ */
