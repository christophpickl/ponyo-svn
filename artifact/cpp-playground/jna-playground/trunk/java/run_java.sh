#!/bin/bash

function exec {
	echo ">> ${1}"
	${1}
}

TARGET_FOLDER=target
LIBPATH=/pntmp/usr/local/lib
CLASSPATH=${TARGET_FOLDER}:lib/jna.jar:lib/platform.jar

mkdir ${TARGET_FOLDER}

exec "javac -classpath ${CLASSPATH} -d ${TARGET_FOLDER} src/jpn/PnOpenNIJava.java"
exec "java -Djna.library.path=${LIBPATH} -classpath ${CLASSPATH} jpn.PnOpenNIJava"
