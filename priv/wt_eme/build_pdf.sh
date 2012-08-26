#!/bin/bash

TARGET=wingtsun_eme
#TARGET=wingtsun_eme-minimal
ROOT=src/trunk/

cd $ROOT
makeindex $TARGET
makeglossaries $TARGET
#pdflatex $TARGET
