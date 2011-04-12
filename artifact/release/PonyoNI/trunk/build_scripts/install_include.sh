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
