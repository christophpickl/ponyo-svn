#pragma once
#ifndef IMAGESAVER_H_
#define IMAGESAVER_H_

#include "common_openni.hpp"
#include "pninclude_opencv.h"
#include "ImageConverter.hpp"

namespace pn {
class ImageSaver {
public:
	ImageSaver(ImageConverter*);
	virtual ~ImageSaver();

	/** Stores image to some predefined folder with a timestamp in its filename. */
	void saveToDefault(const xn::ImageMetaData* imageMetaData, const std::string& fileNameSuffix);
	void saveToFile(const xn::ImageMetaData* imageMetaData, const std::string& targetFileName);

private:
	static Log* LOG;
	ImageConverter* converter;
};
}

#endif /* IMAGESAVER_H_ */
