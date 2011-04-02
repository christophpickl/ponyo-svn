
LIB_DIRS=
INC_DIRS=
USED_LIBS=OpenNI
TARGET=app.exe

OSTYPE := $(shell uname -s)
ifeq ("$(OSTYPE)","Darwin")
	INC_DIRS += /usr/include/ni
	LIB_DIRS += /usr/local/lib
	LIB_DIRS += /usr/lib
#	LDFLAGS += -framework CoreFoundation -framework IOKit
else # we assume a linux ;) ubuntu, preferrably
	INC_DIRS += /????
	LIB_DIRS += /????
	LIB_DIRS += /????
endif

INC_DIRS_OPTION = $(foreach dir,$(INC_DIRS),-I$(dir))
LIB_DIRS_OPTION = $(foreach dir,$(LIB_DIRS),-L$(dir))# -L$(OUT_DIR)
USED_LIBS_OPTION = $(foreach lib,$(USED_LIBS),-l$(lib))

#############################################################################
# Targets
#############################################################################
.PHONY: all

#SRCS=$(wildcard *.c)
#OBJS=$(SRCS:.c=.o)
#all: $(OBJS)

#SRC = $(wildcard '*.c')
CFLAGS = $(INC_DIRS_OPTION) $(LIB_DIRS_OPTION) $(USED_LIBS_OPTION) 

all: app.o
	-@echo "Linking ..."
#    g++ -o $@ $^ $(CFLAGS)
	g++ -o $(TARGET) $(LIB_DIRS_OPTION) $(USED_LIBS_OPTION) app.o

#$(TARGET): $(SRC)
app.o:
	-@echo "Compiling ..."
	g++ -c -o app.o $(INC_DIRS_OPTION) app.cpp

clean:
	rm -f app.exe app.o
	
#CPP_FILES := $(wildcard src/*.cpp)
#OBJ_FILES := $(addprefix obj/,$(notdir $(CPP_FILES:.cpp=.o)))
#LD_FLAGS := ...
#CC_FLAGS := ...
#main.exe: $(OBJ_FILES)
#	g++ $LD_FLAGS -o $@ $<
#obj/%.o: src/%cpp
#	g++ $CC_FLAGS -c -o $@ $<