CFLAGS=-fPIC -g -Wall `pkg-config --cflags opencv`
LIBS = `pkg-config --libs opencv`
INCLUDE = -I/usr/local/include/libfreenect
FREE_LIBS = -L/usr/local/lib -lfreenect


all: app
	-@echo "DONE!"
	
app: app.o
	-@echo "Compiling..."
	g++ $(INCLUDE) $(CFLAGS) -o app app.o $(LIBS) $(FREE_LIBS)
	# $(CXX) $(INCLUDE) $(CFLAGS) $? -o $@  $(LIBS) $(FREE_LIBS)

app.o:
	g++ -c $(CFLAGS) -o app.o app.cpp
	
#%.o: %.cpp
#    $(CXX) -c $(CFLAGS) $< -o $@

clean:
	rm -f *.o app