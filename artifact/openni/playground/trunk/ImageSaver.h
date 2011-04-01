#ifndef IMAGESAVER_H_
#define IMAGESAVER_H_

#include <string.h>
#include <XnCppWrapper.h>

//#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
// highgui contains imwrite function
#include <opencv2/highgui/highgui.hpp>
//#include <opencv2/imgproc/imgproc.hpp>
//#include <opencv2/features2d/features2d.hpp>

class ImageSaver {
public:
	ImageSaver();
	virtual ~ImageSaver();

	void saveToFile(const xn::ImageMetaData&, const std::string&);
};

#endif /* IMAGESAVER_H_ */
