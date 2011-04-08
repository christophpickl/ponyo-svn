CMAKE_MINIMUM_REQUIRED (VERSION 2.8) 
PROJECT (playgroundx)

# LIB - OpenNI
##############################################################
FIND_LIBRARY (OpenNI_LIBRARY OpenNI )
SET (EXTRA_LIBS ${EXTRA_LIBS} ${OpenNI_LIBRARY})
INCLUDE_DIRECTORIES (/usr/include/ni)

# solve "_xnShutdown" undefined symbol link error
SET (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2")

##############################################################

SET (EXECUTABLE1 "main_create_product_nodes")
SET (EXECUTABLE2 "main_usbfail_approach")
SET (EXECUTABLE3 "main_bug_reproduction")

ADD_EXECUTABLE (${EXECUTABLE1} ${EXECUTABLE1}.cpp)
ADD_EXECUTABLE (${EXECUTABLE2} ${EXECUTABLE2}.cpp)
ADD_EXECUTABLE (${EXECUTABLE3} ${EXECUTABLE3}.cpp)

TARGET_LINK_LIBRARIES (${EXECUTABLE1} ${EXTRA_LIBS})
TARGET_LINK_LIBRARIES (${EXECUTABLE2} ${EXTRA_LIBS})
TARGET_LINK_LIBRARIES (${EXECUTABLE3} ${EXTRA_LIBS})

