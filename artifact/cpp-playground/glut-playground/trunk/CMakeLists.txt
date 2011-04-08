CMAKE_MINIMUM_REQUIRED (VERSION 2.8) 
PROJECT (glutplayground)


# LIB - Glut
##############################################################

INCLUDE_DIRECTORIES( /System/Library/Frameworks )
FIND_LIBRARY(GLUT_LIBRARY GLUT )
FIND_LIBRARY(OpenGL_LIBRARY OpenGL )
MARK_AS_ADVANCED (GLUT_LIBRARY OpenGL_LIBRARY)
SET(EXTRA_LIBS ${EXTRA_LIBS} ${GLUT_LIBRARY} ${OpenGL_LIBRARY})

# LIB - OpenNI
##############################################################
INCLUDE_DIRECTORIES (/usr/include/ni)
FIND_LIBRARY (OpenNI_LIBRARY OpenNI )
SET (EXTRA_LIBS ${EXTRA_LIBS} ${OpenNI_LIBRARY})

# solve "_xnShutdown" undefined symbol link error
SET (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2")

##############################################################

SET (EXECUTABLE1 "app")
SET (EXECUTABLE2 "simplewin")

ADD_EXECUTABLE (${EXECUTABLE1} ${EXECUTABLE1}.cpp HelloGlutTriangle.cpp KinectWindow.cpp)
ADD_EXECUTABLE (${EXECUTABLE2} ${EXECUTABLE2}.cpp)

TARGET_LINK_LIBRARIES (${EXECUTABLE1} ${EXTRA_LIBS})
TARGET_LINK_LIBRARIES (${EXECUTABLE2} ${EXTRA_LIBS})

