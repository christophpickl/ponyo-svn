
# GENERAL SETUP
###############################################################################
	CMAKE_MINIMUM_REQUIRED (VERSION 2.8) 
	PROJECT (PonyoNI)

# COMPILER FLAGS
###############################################################################
	SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2") # prevent "Undefined symbols" error
	#SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g")
	#SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall")

# MACRO: SETUP LIB
###############################################################################
MACRO (PN_SETUP_LIB LIBNAME)
    INCLUDE_DIRECTORIES ("${PROJECT_SOURCE_DIR}/${LIBNAME}/src")
	ADD_SUBDIRECTORY (${LIBNAME})
	SET (EXTRA_LIBS ${EXTRA_LIBS} ${LIBNAME})
ENDMACRO (PN_SETUP_LIB) 

PN_SETUP_LIB ("PnCommon")

ADD_SUBDIRECTORY (PnSamples)
