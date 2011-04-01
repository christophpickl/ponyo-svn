SOMEFLAGS = -arch i386 -arch x86_64 -DNDEBUG -msse3 -lOpenNI
#OPENCV_LIBS =  -lopencv_core -lopencv_imgproc -lopencv_calib3d -lopencv_video -lopencv_features2d -lopencv_ml -lopencv_highgui -lopencv_objdetect -lopencv_contrib -lopencv_legacy
OPENCV_LIBS =  -lopencv_core -lopencv_highgui
CXXFLAGS =	-O2 -g -Wall -fmessage-length=0 -I/usr/include/ni -I/usr/local/include/opencv/ $(SOMEFLAGS) $(OPENCV_LIBS)

OBJS = nienhance.o ponyo_common.o NiEnhanced.o MultipleKinects.o

# OpenCV
#
# sample compilation using pkg-config: g++ -o my_example my_example.cpp `pkg-config opencv --cflags --libs`
# opencv libs are located in /usr/!LOCAL!/lib

LIBS = -L/usr/lib -L/usr/local/lib

TARGET = nienhance.exe

$(TARGET):	$(OBJS)
	$(CXX) $(CXXFLAGS) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
