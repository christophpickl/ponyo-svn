#!/bin/bash

MATLAB_INSTALL_PATH=/Applications/Develop/MATLAB_R2010b.app
MATLAB_PROJECT_PATH=../../matlab-mex-playground
SRC_PATH=../src

IS_POSTWORK_ENABLED=1
IS_SAY_ENABLED=0

echo # give it some empty line at the top

function build_mexes {
	IS_POSTWORK_ENABLED=0
	
	for CURRENT_MEX_FUNCTION in "${MEX_FUNCTIONS[@]}"
	do :
		MEX_FUNCTION=${CURRENT_MEX_FUNCTION}
		build_mex
	done
	
	postwork_notification
}

function build_mex {
	echo "[INFO] ---> Building mex function '${MEX_FUNCTION}' ..."
	
	
	CPP_FILENAME=pn_mex_common.cpp
	CPP_PATH=${SRC_PATH}/${CPP_FILENAME}
	OBJ_FILENAME=pn_mex_common.o
	OBJ_PATH=${SRC_PATH}/${OBJ_FILENAME}
	compile_mex
	
	
	CPP_FILENAME=${MEX_FUNCTION}.cpp
	CPP_PATH=${SRC_PATH}/${CPP_FILENAME}
	OBJ_FILENAME=${MEX_FUNCTION}.o
	# TODO create temporary build directory!
	OBJ_PATH=${SRC_PATH}/${OBJ_FILENAME}
	
	OUT_FILENAME=${MEX_FUNCTION}.mexmaci64
	OUT_PATH=${MATLAB_PROJECT_PATH}/${OUT_FILENAME}
	
	clean_mex
	compile_mex
	link_mex
	
	echo 
	if [ ${IS_POSTWORK_ENABLED} -eq 1 ]; then
		postwork_notification
	fi
}

function clean_mex {
	echo "[DEBUG] Cleaning ..."
	# TODO clean temp build dir as well!
	
	rm -f ${OBJ_PATH}
	rm -f ${OUT_PATH} # delete the *.mexmaci64 file
}

function compile_mex {
	echo "[INFO] Compiling ${CPP_FILENAME} ..."
	
	COMP_CMD="-c"
	COMP_CMD="${COMP_CMD} -I${MATLAB_INSTALL_PATH}/extern/include"
	COMP_CMD="${COMP_CMD} -I${MATLAB_INSTALL_PATH}/simulink/include"
	COMP_CMD="${COMP_CMD} -arch x86_64" # someone (gnu or mex) complained about something being wrong with archetypes ... -arch i386 -arch x86_64
	COMP_CMD="${COMP_CMD} -fno-common -no-cpp-precomp -fexceptions"
	COMP_CMD="${COMP_CMD} -msse3 -DNDEBUG -O2"
	# ... delme?! -g -Wall -fmessage-length=0
	COMP_CMD="${COMP_CMD} -o ${OBJ_PATH}"
	COMP_CMD="${COMP_CMD} ${CPP_PATH}"
	
	echo "[DEBUG] >> g++ ${COMP_CMD}"
	g++ ${COMP_CMD}
	
	if [ $? -ne 0 ]; then
		echo
		echo "[ERROR] Compile error! Aborting."
		
		if [ ${IS_SAY_ENABLED} -eq 1 ]; then
			say -v "Pipe Organ" "There was a fatal compile error!"
		fi
		
		exit 1
	fi
}

function link_mex {
	echo "[INFO] Linking ${OBJ_PATH} to ${OUT_PATH} ..."
	
	LINK_CMD="-g"
	LINK_CMD="${LINK_CMD} -o ${OUT_PATH} ${OBJ_PATH} ../src/pn_mex_common.o"
	LINK_CMD="${LINK_CMD} -arch x86_64"
	LINK_CMD="${LINK_CMD} -L${MATLAB_INSTALL_PATH}/bin/maci64"
	LINK_CMD="${LINK_CMD} -Wl,-syslibroot,/Developer/SDKs/MacOSX10.6.sdk"
	LINK_CMD="${LINK_CMD} -bundle"
	LINK_CMD="${LINK_CMD} -Wl,-exported_symbols_list,${MATLAB_INSTALL_PATH}/extern/lib/maci64/mexFunction.map"
	LINK_CMD="${LINK_CMD} -lstdc++"
	LINK_CMD="${LINK_CMD} -lmx -lmex -lmat"
	LINK_CMD="${LINK_CMD} -mmacosx-version-min=10.6"
	echo "[DEBUG] >> g++ ${LINK_CMD}"
	g++ ${LINK_CMD}
	
	if [ $? -ne 0 ]; then
		echo
		echo "[ERROR] Link error! Aborting."
		
		if [ ${IS_SAY_ENABLED} -eq 1 ]; then
			say -v "Pipe Organ" "There was a fatal linking error!"
		fi
		exit 1
	fi
}

function postwork_notification {
	echo "[INFO] ================================================="
	echo "[INFO]               Finished successfully"
	echo "[INFO] ================================================="
	
	if [ ${IS_SAY_ENABLED} -eq 1 ]; then
		say -v Vicki "Finished successfully."
	fi
	
	osascript -e "tell application \"MATLAB_R2010b\"" -e "activate" -e "end tell"
}
