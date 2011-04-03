
MATLAB_INSTALL_PATH=/Applications/Develop/MATLAB_R2010b.app
INCLUDES += -I$(MATLAB_INSTALL_PATH)/extern/include
INCLUDES += -I$(MATLAB_INSTALL_PATH)/simulink/include
LDFLAGS = -L$(MATLAB_INSTALL_PATH)/bin/maci64 -lstdc++ -leng -fno-common -no-cpp-precomp -fexceptions -arch x86_64 -arch i386  -mmacosx-version-min=10.6 -Wl,-syslibroot,/Developer/SDKs/MacOSX10.6.sdk
# -isysroot $SDKROOT 

# see: http://www.mathworks.com/help/techdoc/matlab_external/f39903.html
#LDFLAGS = -L$(MATLAB_INSTALL_PATH)/bin/maci64 -lmx -lmex -lmat -Wl,-exported_symbols_list,$(MATLAB_INSTALL_PATH)/extern/lib/maci64/mexFunction.map

all: app
	-@echo all doing...
	
app: app.o
	-@echo "Linking ..."
	g++ -o app $(LDFLAGS) app.o

app.o:
	-@echo "Compiling ..."
	g++ -c -o app.o $(INCLUDES) app.cpp

clean:
	rm -f app.o app