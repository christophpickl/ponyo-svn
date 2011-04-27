#!/bin/bash

source ../build_scripts/install_include.sh

SCRIPT_DIR=`pwd`/`dirname $0`
TARGET=${SCRIPT_DIR}/target
BUILD_FINAL_NAME=PonyoNI-${PN_VERSION}
TARGET_BUILD=${TARGET}/${BUILD_FINAL_NAME}

echo "[DEBUG] TARGET: $TARGET"

exec "rm -rf $TARGET"
exec "mkdir -p $TARGET_BUILD"

############ BUILD OscPack
exec "cmake ../OscPack -DCMAKE_INSTALL_PREFIX:PATH=${TARGET_BUILD}"
exec "cd ${SCRIPT_DIR}/../OscPack"
exec "make"
exec "make install"

############ BUILD PonyoNI
exec "cmake ../ -DCMAKE_INSTALL_PREFIX:PATH=${TARGET_BUILD}"
exec "cd ${SCRIPT_DIR}/../"
exec "make"
exec "make install"


############ ADD additional resources
exec "cd $SCRIPT_DIR"
exec "cp additional_resources/* ${TARGET_BUILD}/"
exec "chmod +x ${TARGET_BUILD}/install.sh"


############ PACKAGE
exec "cd ${TARGET}"
exec "zip -r ${BUILD_FINAL_NAME}.zip ${BUILD_FINAL_NAME}"


############ FINISHED
exec "cd ${SCRIPT_DIR}"
echo
echo "*** DONE ***"
echo