
EXECUTABLES += templates
EXECUTABLES += datastructures
EXECUTABLES += interfaces
EXECUTABLES += exceptions
SOURCES += Util.cpp
SOURCES += Exception.cpp
SOURCES += ConnectionException.cpp
SOURCES += DummyServiceListener.cpp
SOURCES += MyService.cpp

INCLUDES += /usr/include
INCLUDES += /usr/local/include
CXX=g++

INCLUDES_MERGED = $(foreach i,$(INCLUDES),-I$(i))
EXECUTABLES_MERGED = $(foreach i,$(EXECUTABLES),$(i))
CFLAGS = -c -Wall $(INCLUDES_MERGED)
LFLAGS = #-L/usr/lib -L/usr/local/lib
TARGET=target
OBJECTS =$(SOURCES:.cpp=.o)
OBJECTS_PATH = $(foreach i,$(OBJECTS),$(TARGET)/$(i))

.PHONY: all init clean

all: init $(SOURCES) $(EXECUTABLES_MERGED)
	@echo "================================================="
	@echo

init:
	@echo "CFLAGS: $(CFLAGS)"
	@echo "INCLUDES_MERGED: $(INCLUDES_MERGED)"
	@echo "LFLAGS: $(LFLAGS)"
	@echo "OBJECTS: $(OBJECTS)"
	@echo "EXECUTABLES_MERGED: $(EXECUTABLES_MERGED)"
	@echo
	mkdir -p $(TARGET)

templates: $(OBJECTS) main_templates.o
	$(CXX) $(LFLAGS) $(OBJECTS_PATH) $(TARGET)/main_$(@).o -o $(TARGET)/$@
	
datastructures: $(OBJECTS) main_datastructures.o
	$(CXX) $(LFLAGS) $(OBJECTS_PATH) $(TARGET)/main_$(@).o -o $(TARGET)/$@

interfaces: $(OBJECTS) main_interfaces.o
	$(CXX) $(LFLAGS) $(OBJECTS_PATH) $(TARGET)/main_$(@).o -o $(TARGET)/$@

exceptions: $(OBJECTS) main_exceptions.o
	$(CXX) $(LFLAGS) $(OBJECTS_PATH) $(TARGET)/main_$(@).o -o $(TARGET)/$@

%.o: %.cpp
	$(CXX) $(CFLAGS) $(INCLUDES_MERGED) -c $< -o $(TARGET)/$@

clean:
	rm -rf $(TARGET)
