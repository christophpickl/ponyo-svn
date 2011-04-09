#!/bin/bash

cmake -DCMAKE_INSTALL_PREFIX:PATH=/usr/local
#cmake -DCMAKE_INSTALL_PREFIX:PATH=/pxtmp2/usr/local

#cmake -DCMAKE_INSTALL_PREFIX:PATH=/pxtmp \
#      -DCMAKE_INSTALL_NAME_DIR:STRING=@executable_path/../somelib .