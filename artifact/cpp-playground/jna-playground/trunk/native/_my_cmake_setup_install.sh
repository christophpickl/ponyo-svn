#!/bin/bash

#TARGET=/usr/local
TARGET=/pntmp/usr/local

echo "Building and installing PnJNA to ${TARGET}"

cmake -DCMAKE_INSTALL_PREFIX:PATH=${TARGET}
make
make install

echo
echo "FINISHED!"
