#pragma once
#ifndef IMAGESAVER_H_
#define IMAGESAVER_H_

#include "common_openni.hpp"
#include "pninclude_opencv.h"

namespace pn {
class ImageSaver {
public:
	ImageSaver();
	virtual ~ImageSaver();

	/** Stores image to some predefined folder with a timestamp in its filename. */
	void saveToDefault(const xn::ImageMetaData*, const std::string&);
	void saveToFile(const xn::ImageMetaData*, const std::string&);

private:
	static Log* LOG;
};
}

#endif /* IMAGESAVER_H_ */
