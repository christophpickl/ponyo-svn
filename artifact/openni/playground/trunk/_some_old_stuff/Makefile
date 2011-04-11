SOMEFLAGS = -arch i386 -arch x86_64 -DNDEBUG -msse3 -lOpenNI
CXXFLAGS =	-O2 -g -Wall -fmessage-length=0 -I/usr/include/ni $(SOMEFLAGS)

OBJS = nienhance.o NiEnhanced.o

LIBS = -L/usr/lib

TARGET = nienhance.exe

$(TARGET):	$(OBJS)
	$(CXX) $(CXXFLAGS) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
