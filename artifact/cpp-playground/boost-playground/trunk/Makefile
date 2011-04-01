CXXFLAGS =	-O2 -g -Wall -fmessage-length=0 -L/usr/local/lib -I/usr/local/include
OBJS = app.o
LIBS = -lboost_thread
TARGET = app

$(TARGET):	$(OBJS)
	$(CXX) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
