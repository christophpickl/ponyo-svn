#!/bin/bash

echo "compiling ..."
g++ -c \
	-I/usr/include/ni \
	-I/Applications/Develop/MATLAB_R2010b.app/extern/include \
	-I/Applications/Develop/MATLAB_R2010b.app/simulink/include \
	-arch i386 -arch x86_64 \
	-fno-common -no-cpp-precomp -fexceptions \
	-msse3 \
	-DNDEBUG \
	-O2 \
	"mxNiCreateContext.cpp"

echo "linking ..."

g++ -g \
	-o  "mxNiCreateContext.mexmaci64"  mxNiCreateContext.o \
	-arch i386 -arch x86_64 \
	-L/usr/lib \
	-lOpenNI \
	-L/Applications/Develop/MATLAB_R2010b.app/bin/maci64 \
	-Wl,-syslibroot,/Developer/SDKs/MacOSX10.6.sdk \
	-bundle \
	-Wl,-exported_symbols_list,/Applications/Develop/MATLAB_R2010b.app/extern/lib/maci64/mexFunction.map \
	-lstdc++ \
	-lmx \
	-lmex \
	-lmat \
	-mmacosx-version-min=10.6