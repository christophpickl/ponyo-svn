#!/bin/bash

CHECK() {
	RETURN_CODE=$1
	if [ ${RETURN_CODE} -ne 0 ]; then
		echo FAIL!!! RETURN_CODE was: ${RETURN_CODE}
		exit 1 
	fi
}

cmake -DCMAKE_INSTALL_PREFIX:PATH=/usr/local
#cmake -DCMAKE_INSTALL_PREFIX:PATH=/pxtmp/usr/local
CHECK $?

make
CHECK $?

make install
CHECK $?
