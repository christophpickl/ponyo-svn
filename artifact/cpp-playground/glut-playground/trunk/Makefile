SOME_NI_FLAGS = -arch i386 -arch x86_64 -DNDEBUG -msse3
C_AND_LDFLAGS = -I/System/Library/Frameworks/GLUT.framework/Headers -I/usr/include/ni
CXXFLAGS =-O2 -g -Wall -fmessage-length=0 $(C_AND_LDFLAGS) $(SOME_NI_FLAGS)

OBJS = KinectWindow.o HelloGlutTriangle.o app.o
LIBS = -framework OpenGL -framework GLUT -L/usr/lib -L/usr/local/lib -lOpenNI
TARGET = app

$(TARGET):	$(OBJS)
	$(CXX) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
