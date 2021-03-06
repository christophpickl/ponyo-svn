#!/bin/bash

TARGET=/usr/local
#TARGET=/pntmp/usr/local

source build_scripts/install_include.sh


echo "Building and installing ponyo libs to ${TARGET}"

# one could force cmake using g++ ;) -DCMAKE_CXX_COMPILER=g++

exec "cmake --debug-output ."
#exec "make clean"
exec "cmake -DCMAKE_INSTALL_PREFIX:PATH=${TARGET}"
#      -DCMAKE_INSTALL_NAME_DIR:STRING=@executable_path/../somelib .
exec "make"
exec "make install"

echo "[INFO]"
echo "[INFO] FINISHED SUCCESSFULLY! :)"

exit 0