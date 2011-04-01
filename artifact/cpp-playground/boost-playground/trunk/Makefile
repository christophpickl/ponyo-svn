CXXFLAGS =	-O2 -g -Wall -fmessage-length=0
OBJS = app.o
LIBS =
TARGET = app

$(TARGET):	$(OBJS)
	$(CXX) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
