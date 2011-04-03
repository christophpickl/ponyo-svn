
EXECUTABLES += templates
EXECUTABLES += datastructures
EXECUTABLES += interfaces
EXECUTABLES += exceptions
SOURCES += Util.cpp
SOURCES += Exception.cpp
SOURCES += ConnectionException.cpp
SOURCES += DummyServiceListener.cpp
SOURCES += MyService.cpp
OBJECTS=$(SOURCES:.cpp=.o)

INCLUDES += /usr/include
INCLUDES += /usr/local/include
CC=g++

INCLUDES_MERGED = $(foreach dir,$(INCLUDES),-I$(dir))
EXECUTABLES_MERGED = $(foreach dir,$(EXECUTABLES),$(dir))
CFLAGS = -c -Wall $(INCLUDES_MERGED)
LFLAGS = #-L/usr/lib -L/usr/local/lib

.PHONY: all clean

all: $(SOURCES) $(EXECUTABLES_MERGED)
	@echo "executables: $(EXECUTABLES_MERGED)"
	@echo "================================================="
	@echo

templates: $(OBJECTS) main_templates.o
	$(CC) $(LFLAGS) $(OBJECTS) main_$(@).o -o $@
	
datastructures: $(OBJECTS) main_datastructures.o
	$(CC) $(LFLAGS) $(OBJECTS) main_$(@).o -o $@

interfaces: $(OBJECTS) main_interfaces.o
	$(CC) $(LFLAGS) $(OBJECTS) main_$(@).o -o $@

exceptions: $(OBJECTS) main_exceptions.o
	$(CC) $(LFLAGS) $(OBJECTS) main_$(@).o -o $@

%.o: %.cpp
	$(CC) $(CFLAGS) $(INCLUDES_MERGED) -c $< -o $@

clean:
	rm -f $(EXECUTABLES_MERGED) *.o
