#!/bin/bash

MATLAB_INSTALL_PATH=/Applications/Develop/MATLAB_R2010b.app
MATLAB_PROJECT_PATH=../matlab-mex-playground
MEX_FUNCTION=mxHello
MEX_SOURCE=${MEX_FUNCTION}.cpp
TARGET=mxHello.mexmaci64


echo "Cleaning ..."
rm -f ${MEX_FUNCTION}.o

echo "Compiling ..."

# someone (gnu or mex) complained about something being wrong with archetypes ... -arch i386 -arch x86_64

g++ -c \
	-I${MATLAB_INSTALL_PATH}/extern/include \
	-I${MATLAB_INSTALL_PATH}/simulink/include \
	-arch x86_64 \
	-fno-common -no-cpp-precomp -fexceptions \
	-msse3 \
	-DNDEBUG \
	-O2 \
	"${MEX_SOURCE}"

if [ $? -ne 0 ]; then
	echo
	echo "Compile error! Aborting."
	exit 1
fi

echo "Linking ..."

g++ -g \
	-o  "${TARGET}"  mxHello.o \
	-arch x86_64 \
	-L${MATLAB_INSTALL_PATH}/bin/maci64 \
	-Wl,-syslibroot,/Developer/SDKs/MacOSX10.6.sdk \
	-bundle \
	-Wl,-exported_symbols_list,${MATLAB_INSTALL_PATH}/extern/lib/maci64/mexFunction.map \
	-lstdc++ \
	-lmx \
	-lmex \
	-lmat \
	-mmacosx-version-min=10.6


if [ $? -ne 0 ]; then
	echo
	echo "Link error! Aborting."
	exit 1
fi

FULL_TARGET=${MATLAB_PROJECT_PATH}/${TARGET}
echo "Moving output to: ${FULL_TARGET}"
rm -f ${MATLAB_PROJECT_PATH}/${TARGET}
mv ${TARGET} ${FULL_TARGET} 

echo
echo "Finished successfully."
say "Finished successfully."

osascript -e "tell application \"MATLAB_R2010b\"" -e "activate" -e "end tell"