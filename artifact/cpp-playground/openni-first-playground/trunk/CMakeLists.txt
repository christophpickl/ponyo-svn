CMAKE_MINIMUM_REQUIRED (VERSION 2.8) 
PROJECT (playgroundx)
SET (EXECUTABLE_OUTPUT_PATH bin)

# LIB - OpenNI
##############################################################
FIND_LIBRARY (OpenNI_LIBRARY OpenNI )
SET (EXTRA_LIBS ${EXTRA_LIBS} ${OpenNI_LIBRARY})
INCLUDE_DIRECTORIES (/usr/include/ni)

# solve "_xnShutdown" undefined symbol link error
SET (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2")

##############################################################
ADD_EXECUTABLE (nienhance nienhance.cpp)
TARGET_LINK_LIBRARIES (nienhance ${EXTRA_LIBS})


