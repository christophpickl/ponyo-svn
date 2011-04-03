
INCLUDES += /usr/include
INCLUDES += /usr/local/include

_INCLUDES_MERGED = $(foreach dir,$(INCLUDES),-I$(dir))
_CFLAGS = $(_INCLUDES_MERGED)
all: interfaces exceptions

interfaces: MyService.o main_interfaces.o
	g++ $(_CFLAGS) MyService.o main_interfaces.o -o interfaces

main_interfaces.o:
	g++ -c $(_CFLAGS) -o main_interfaces.o main_interfaces.cpp
	
MyService.o:
	g++ -c $(_CFLAGS) -o MyService.o MyService.cpp

exceptions:
	g++ $(_CFLAGS) -o exceptions main_exceptions.cpp

clean:
	rm -f *.o
	rm interfaces exceptions