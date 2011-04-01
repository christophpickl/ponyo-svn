#ifndef IMAGESAVER_H_
#define IMAGESAVER_H_

#include <string.h>
#include <XnCppWrapper.h>

// we want to be very specific about what we want to include #include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
// highgui contains imwrite function
#include <opencv2/highgui/highgui.hpp>

class ImageSaver {
public:
	ImageSaver();
	virtual ~ImageSaver();

	/** Stores image to some predefined folder with a timestamp in its filename. */
	void saveToDefault(const xn::ImageMetaData&, const std::string&);
	void saveToFile(const xn::ImageMetaData&, const std::string&);
};

#endif /* IMAGESAVER_H_ */
