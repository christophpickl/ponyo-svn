#include <stdio.h>

// we want to be very specific about what we want to include #include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
// highgui contains imwrite function
#include <opencv2/highgui/highgui.hpp>

#define IMG_BLUE_TEMPLATE "images/blue_ball_template.jpg"
#define IMG_RED_TEMPLATE "images/red_ball_template.jpg"
#define IMG_SNAPSHOT1 "images/snapshot1.jpg"
#define IMG_SNAPSHOT2 "images/snapshot2.jpg"

/**
 * @param matchTemplateMethod one of: { CV_TM_SQDIFF, CV_TM_SQDIFF_NORMED, CV_TM_CCORR, CV_TM_CCORR_NORMED, CV_TM_CCOEFF, CV_TM_CCOEFF_NORMED }
 *
 * some links:
 * - http://opencv.willowgarage.com/documentation/cpp/imgproc_object_detection.html
 * - minMaxLoc API Doc ... http://opencv.willowgarage.com/documentation/cpp/core_operations_on_arrays.html#minMaxLoc
 * - http://www.quotientrobotics.com/2010_02_01_archive.html
 * - https://code.ros.org/trac/opencv/changeset/4465
 *
 */
cv::Point findImage(cv::Mat& matSource, cv::Mat& matTemplate, int matchTemplateMethod = CV_TM_SQDIFF) /*throw(Some..Exception)*/ {
	printf("findImage(..)\n");
	cv::Mat matResult;
	cv::matchTemplate(matSource, matTemplate, matResult, matchTemplateMethod);
	// After the function finishes the comparison, the best matches can be found as
	// global minimums (when CV_TM_SQDIFF was used) or maximums (when CV_TM_CCORR or CV_TM_CCOEFF was used)
	// using the minMaxLoc() function. In the case of a color image, template summation in the numerator and
	// each sum in the denominator is done over all of the channels (and separate mean values are used for each channel).
	// That is, the function can take a color template and a color image; the result will still be a single-channel image, which is easier to analyze.

	double minValue;
	double maxValue;
	cv::Point minLoc;
	cv::Point maxLoc;

	try {
		cv::minMaxLoc(matResult, &minValue, &maxValue, &minLoc, &maxLoc/*, const Mat& mask=Mat()*/);
		printf("minMaxLoc result:\n\t- minValue: %f\n\t- maxValue: %f\n\t- minLoc: %ix%i\n\t- maxLoc: %ix%i\n\n",
				minValue, maxValue, minLoc.x, minLoc.y, maxLoc.x, maxLoc.y);

	} catch(cv::Exception& ex) {
		fprintf(stderr, "cv::minMaxLoc() has thrown exception: %s\n", ex.what());
		throw ex;
	}

	if(matchTemplateMethod == CV_TM_SQDIFF) {
		return minLoc;
	}
	if(matchTemplateMethod == CV_TM_CCORR || matchTemplateMethod == CV_TM_CCOEFF) {
		return maxLoc;
	}
	throw "uje, not supported template method :(";
}

int main() {
	printf("main() START\n");

	// Mat image = imread(std::string(ts->get_data_path()) + "matchtemplate/black.jpg");
	// Mat image = imread(file, 0);
	// http://www.cognotics.com/opencv/servo_2007_series/part_2/index.html
	// image.convertTo(double_I, CV_64F); ... http://efreedom.com/Question/1-3188352/Changing-Dataype-Mat-Class-Instance-OpenCV-CPlusPlus-Interface

	printf("Loading images ...\n");
	IplImage* imgSource = cvLoadImage(IMG_SNAPSHOT2);
	IplImage* imgTemplate = cvLoadImage(IMG_BLUE_TEMPLATE);
	cv::Mat matSource(imgSource);
	cv::Mat matTemplate(imgTemplate);

	cv::Point p = findImage(matSource, matTemplate);
	// blue ball on approx 250x120
	printf("findImage(..) ... result: %ix%i\n", p.x, p.y);

	cvNamedWindow("Fooo Window", CV_WINDOW_AUTOSIZE);
	cvShowImage("Source Image", imgSource);
	cvShowImage("Template Image", imgTemplate);

	printf(">> cvWaitKey\n");
	cvWaitKey(0);

	cvDestroyWindow("Fooo Window");
	cvReleaseImage(&imgSource);
	cvReleaseImage(&imgTemplate);

	printf("main() END\n");
	return 0;
}
