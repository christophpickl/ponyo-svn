#!/bin/bash

TARGET=wteme-minimal
#TARGET=wteme-slim
#TARGET=wteme-full
#TARGET=wteme-development
ROOT=src/trunk/

cd $ROOT
makeindex $TARGET
makeglossaries $TARGET
#pdflatex $TARGET
