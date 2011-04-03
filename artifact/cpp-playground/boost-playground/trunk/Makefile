#lboost_thread-mt
CXXFLAGS = -O2 -g -Wall -L/usr/local/lib -I/usr/local/include -I/System/Library/Frameworks/GLUT.framework/Headers
LIBS = -lboost_thread -framework OpenGL -framework GLUT -L/usr/lib -L/usr/local/lib

#OBJS = app.o
#TARGET = app

OBJS += Lockable.o
OBJS += DataSnapshot.o
OBJS += DeviceDataGobbler.o
OBJS += main_threading.o
TARGET = main_threading


all: $(TARGET)

$(TARGET): $(OBJS)
	$(CXX) -o $(TARGET) $(OBJS) $(LIBS)

%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

clean:
	rm -f $(OBJS) $(TARGET)
