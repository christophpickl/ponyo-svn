CMAKE_MINIMUM_REQUIRED (VERSION 2.8) 
PROJECT (playgroundx)

# LIB - OpenNI
	FIND_LIBRARY (OpenNI_LIBRARY OpenNI)
	SET (EXTRA_LIBS ${EXTRA_LIBS} ${OpenNI_LIBRARY})
	INCLUDE_DIRECTORIES (/usr/include/ni)

# LIB - OpenCV
	FIND_PACKAGE (OpenCV REQUIRED)
	SET (EXTRA_LIBS ${EXTRA_LIBS} ${OpenCV_LIBS})

SET (EXECUTABLE_OUTPUT_PATH bin)
SET (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2") # solve "_xnShutdown" undefined symbol link error

##############################################################
ADD_EXECUTABLE (nienhance nienhance.cpp MultipleKinects.cpp NiDevice.cpp ImageSaver)
TARGET_LINK_LIBRARIES (nienhance ${EXTRA_LIBS})


