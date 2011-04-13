MESSAGE ("===> ROOT")

# GENERAL SETUP
###############################################################################
	CMAKE_MINIMUM_REQUIRED (VERSION 2.8) 
	PROJECT (PonyoNI)
	INCLUDE (build_scripts/cmake_common.txt)

PN_SETUP_LIB (PnCommon)
PN_SETUP_LIB (PnOpenNI)
PN_SETUP_LIB (PnJNA)
PN_SETUP_EXE (PnOSC)
PN_SETUP_EXE (PnSamples)

ADD_DEPENDENCIES (PnOpenNI PnCommon)
ADD_DEPENDENCIES (PnJNA PnOpenNI)
#ADD_DEPENDENCIES (PnSamples PnOpenNI)
