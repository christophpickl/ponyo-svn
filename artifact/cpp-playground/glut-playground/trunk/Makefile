C_AND_LDFLAGS = -L/usr/local/lib -I/System/Library/Frameworks/GLUT.framework/Headers
CXXFLAGS =-O2 -g -Wall -fmessage-length=0 $(C_AND_LDFLAGS) -framework OpenGL -framework GLUT

OBJS = app.o
#LIBS = -framework OpenGL -framework GLUT
LIBS = -framework OpenGL -framework GLUT
TARGET = app

$(TARGET):	$(OBJS)
	$(CXX) -o $(TARGET) $(OBJS) $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
