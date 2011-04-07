// SOURCE http://www.cognotics.com/opencv/servo_2007_series/part_2/detect.c

// declarations
CvHaarClassifierCascade * pCascade = 0;  // the face detector
CvMemStorage * pStorage = 0;        // expandable memory buffer
CvSeq * pFaceRectSeq;               // list of detected faces
int i;

// initializations
IplImage * ppImg = (argc > 1) ?
   cvLoadImage(argv[1], CV_LOAD_IMAGE_COLOR) : 0;
pStorage = cvCreateMemStorage(0);
pCascade = (CvHaarClassifierCascade *)cvLoad
  ((OPENCV_ROOT"/data/haarcascades/haarcascade_frontalface_default.xml"),
  0, 0, 0 );

// validate that everything initialized properly
if( !ppImg || !pStorage || !pCascade )
{
   printf("Initialization failed: %s \n",
     (!ppImg)?  "didn't load image file" :
     (!pCascade)? "didn't load Haar cascade -- "
                  "make sure path is correct" :
     "failed to allocate memory for data storage");
   exit(-1);
}

// detect faces in image
pFaceRectSeq = cvHaarDetectObjects
   (ppImg, pCascade, pStorage,
   1.1,                       // increase search scale by 10% each pass
   3,                         // drop groups of fewer than three detections
   CV_HAAR_DO_CANNY_PRUNING,  // skip regions unlikely to contain a face
   cvSize(0,0));              // use XML default for smallest search scale

// create a window to display detected faces
cvNamedWindow("Haar Window", CV_WINDOW_AUTOSIZE);

// draw a rectangular outline around each detection
for(i=0;i<(pFaceRectSeq? pFaceRectSeq->total:0); i++ )
{
   CvRect * r = (CvRect*)cvGetSeqElem(pFaceRectSeq, i);
   CvPoint pt1 = { r->x, r->y };
   CvPoint pt2 = { r->x + r->width, r->y + r->height };
   cvRectangle(ppImg, pt1, pt2, CV_RGB(0,255,0), 3, 4, 0);
}

// display face detections
cvShowImage("Haar Window", ppImg);
cvWaitKey(0);
cvDestroyWindow("Haar Window");

// clean up and release resources
cvReleaseImage(&ppImg);
if(pCascade) cvReleaseHaarClassifierCascade(&pCascade);
if(pStorage) cvReleaseMemStorage(&pStorage);
