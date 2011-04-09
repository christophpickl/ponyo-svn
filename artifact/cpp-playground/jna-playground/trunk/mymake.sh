#!/bin/bash

CWD=`pwd`
echo CWD: ${CWD}

CHECK() {
	RETURN_CODE=$1
	if [ ${RETURN_CODE} -ne 0 ]; then
		echo FAIL!!! RETURN_CODE was: ${RETURN_CODE}
		cd ${CWD}
		exit 1 
	fi
}


cd native
cmake .
CHECK $?
make
CHECK $?
make install
CHECK $?
cd ..

cd java
./run_java.sh
CHECK $?

echo
echo SUCCESS!
cd ${CWD}
exit 0
