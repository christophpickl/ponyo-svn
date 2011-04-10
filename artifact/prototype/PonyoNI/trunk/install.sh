#!/bin/bash

CWD=`pwd`
echo CWD: ${CWD}

function exec {
	echo "[INFO] >> ${1}"
	${1}
	CHECK $?
}

CHECK() {
	RETURN_CODE=$1
	if [ ${RETURN_CODE} -eq 127 ]; then
		echo "[WARN] Ignoring RETURN_CODE=127 (hopefully from cmake -DCMAKE_..."
	elif [ ${RETURN_CODE} -ne 0 ]; then
		echo FAIL!!! RETURN_CODE was: ${RETURN_CODE}
		cd ${CWD}
		exit 1 
	fi
}


TARGET=/usr/local
#TARGET=/pntmp/usr/local

echo "Building and installing ponyo libs to ${TARGET}"

#exec "make clean"

exec "cmake -DCMAKE_INSTALL_PREFIX:PATH=${TARGET}"
#cmake -DCMAKE_INSTALL_PREFIX:PATH=/pxtmp \
#      -DCMAKE_INSTALL_NAME_DIR:STRING=@executable_path/../somelib .

exec "make"

exec "make install"

echo "[INFO]"
echo "[INFO] FINISHED!"


exit 0