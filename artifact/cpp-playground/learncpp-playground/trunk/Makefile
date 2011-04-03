
EXECUTABLES += interfaces
EXECUTABLES += exceptions
SOURCES += Util.cpp
SOURCES += Exception.cpp
SOURCES += ConnectionException.cpp
SOURCES += MyService.cpp
OBJECTS=$(SOURCES:.cpp=.o)

INCLUDES += /usr/include
INCLUDES += /usr/local/include
CC=g++

INCLUDES_MERGED = $(foreach dir,$(INCLUDES),-I$(dir))
EXECUTABLES_MERGED = $(foreach dir,$(EXECUTABLES),$(dir))
CFLAGS = -c -Wall $(INCLUDES_MERGED)
LFLAGS = 

.PHONY: all clean

all: $(SOURCES) $(EXECUTABLES_MERGED)
	@echo "executables: $(EXECUTABLES_MERGED)"

interfaces: $(OBJECTS) main_interfaces.o
	$(CC) $(LFLAGS) $(OBJECTS) main_$(@).o -o $@

exceptions: $(OBJECTS) main_exceptions.o
	$(CC) $(LFLAGS) $(OBJECTS) main_$(@).o -o $@

%.o: %.cpp
	$(CC) $(CFLAGS) $(INCLUDES_MERGED) -c $< -o $@

clean:
	rm -f $(EXECUTABLES_MERGED) *.o
