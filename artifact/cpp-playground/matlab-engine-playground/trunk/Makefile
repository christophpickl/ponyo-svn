# export DYLD_LIBRARY_PATH=/Applications/Develop/MATLAB_R2010b.app/bin/maci64:$DYLD_LIBRARY_PATH
# export PATH="/Applications/Develop/MATLAB_R2010b.app/bin:$PATH"

MATLAB_INSTALL_PATH=/Applications/Develop/MATLAB_R2010b.app

all:
	$(MATLAB_INSTALL_PATH)/bin/mex -f $(MATLAB_INSTALL_PATH)/bin/engopts.sh app.cpp

clean:
	rm -f app.o app