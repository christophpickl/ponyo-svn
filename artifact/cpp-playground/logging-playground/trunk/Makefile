CXXFLAGS = -O2 -g -Wall
LIBS = 

OBJS += Log.o
OBJS += LogFactory.o
OBJS += Foobar.o
OBJS += app.o
TARGET = app

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CXX) -o $(TARGET) $(OBJS) $(LIBS)

%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

clean:
	rm -f $(OBJS) $(TARGET)
