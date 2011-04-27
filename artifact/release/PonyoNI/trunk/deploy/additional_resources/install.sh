#!/bin/bash -e

INSTALL_LIB=/usr/local/lib
INSTALL_BIN=/usr/local/bin
INSTALL_INC=/usr/local/include/ponyo
#INSTALL_VAR=/var/lib/ponyo

INC_FOLDER=${SCRIPT_DIR}/include
LIB_FOLDER=${SCRIPT_DIR}/lib
BIN_FOLDER=${SCRIPT_DIR}/bin

if [ "`uname -s`" == "Darwin" ]; then
	MODULES="libPnCommon.dylib libPnOpenNI.dylib libPnJNA.dylib"
else
	MODULES="libPnCommon.so libPnOpenNI.so libPnJNA.so"
fi

SCRIPT_DIR=`pwd`/`dirname $0`

# read script args
INSTALL="1"

while (( "$#" )); do
	case "$1" in
	"-i")
		INSTALL="1"
		;;
	"-u")
		INSTALL="0"
		;;
	*)
		echo "Usage: $0 [options]"
		echo "Available options:"
		printf "\t-i\tInstall (default)\n"
		printf "\t-u\tUninstall\n"
		exit 1
		;;
	esac
	shift
done

# create file list
LIB_FILES=`ls ${LIB_FOLDER}/*`
BIN_FILES=`ls ${BIN_FOLDER}/*`

if [ "$INSTALL" == "1" ]; then

	# copy libraries
	printf "copying shared libraries..."
	cp $LIB_FILES $INSTALL_LIB
    printf "OK\n"

	# utilities
	printf "copying executables..."
	cp $BIN_FILES $INSTALL_BIN
    printf "OK\n"

	# include files
	printf "copying include files..."
	mkdir -p $INSTALL_INC
	cp -r ${INC_FOLDER}/* $INSTALL_INC
    printf "OK\n"

	# create database dir
#	printf "creating database directory..."
#	mkdir -p $INSTALL_VAR
#   printf "OK\n"

#	for module in $MODULES; do
#		printf "Updating link for '$module'..."
#		ln -s FOO BAR
#		printf "OK\n"
	done

else
	printf "Uninstalling ..."
	
# for module in $MODULES; do
#    	printf "unregistering module '$module'..."
#        if niReg -u $INSTALL_LIB/$module; then
#            printf "OK\n"
#        fi
#	done

	# include files
	printf "removing include files..."
	rm -rf $INSTALL_INC
	printf "OK\n"

	# binaries
	printf "removing executables..."
    for filename in $BIN_FILES; do
        rm -f $INSTALL_BIN/`basename $filename`
    done
	printf "OK\n"

	# libraries
    printf "removing shared libraries..."
    for filename in $LIB_FILES; do
        rm -f $INSTALL_LIB/`basename $filename`
    done
    printf "OK\n"
    
fi

printf "\n*** DONE ***\n\n"
