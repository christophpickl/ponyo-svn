message("==============> ROOT")

cmake_minimum_required (VERSION 2.8) 
project (PonyoNI)

# adding optimizer flag, otherwise build will fail with:
#Undefined symbols:
#  "_xnShutdown", referenced from:
#      xn::Context::Shutdown() 
SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2")








FIND_LIBRARY(OpenNI_LIBRARY OpenNI )
SET(EXTRA_LIBS ${EXTRA_LIBS} ${OpenNI_LIBRARY} )

message("[ROOT] EXTRA_LIBS: ${EXTRA_LIBS}")
#message("CMAKE_CXX_FLAGS: ${CMAKE_CXX_FLAGS}")


IF(APPLE)
	message("APPLE detected, setting OpenNI specific paths")
	INCLUDE_DIRECTORIES( /usr/include/ni )
ENDIF(APPLE)


##############################################################
# PnCommon
##############################################################

include_directories("${PROJECT_SOURCE_DIR}/PnCommon/src")
add_subdirectory(PnCommon)
set(EXTRA_LIBS ${EXTRA_LIBS} PnCommon)


##############################################################
# PnOpenNI
##############################################################

include_directories("${PROJECT_SOURCE_DIR}/PnOpenNI/src")
add_subdirectory(PnOpenNI)
set(EXTRA_LIBS ${EXTRA_LIBS} PnOpenNI)


##############################################################
# PonyoNIApp
##############################################################

add_subdirectory(PonyoNIApp)

message("EXTRA_LIBS: ${EXTRA_LIBS}")
IF(APPLE)
	message("APPLE detected, setting glut specific paths")
   INCLUDE_DIRECTORIES( /System/Library/Frameworks )
   FIND_LIBRARY(GLUT_LIBRARY GLUT )
   FIND_LIBRARY(OpenGL_LIBRARY OpenGL )
   MARK_AS_ADVANCED (GLUT_LIBRARY
                     OpenGL_LIBRARY)
   SET(EXTRA_LIBS ${EXTRA_LIBS} ${GLUT_LIBRARY} ${OpenGL_LIBRARY})
ENDIF (APPLE)
#message("EXTRA_LIBS: ${EXTRA_LIBS}")

set(PONYONIAPP_SRC_FOLDER "${PROJECT_SOURCE_DIR}/PonyoNIApp/src")
file(GLOB_RECURSE CPP_FILES "${PONYONIAPP_SRC_FOLDER}/*.cpp")
message("CPP_FILES: ${CPP_FILES}")
add_executable (PonyoNIApp.exe ${CPP_FILES})
target_link_libraries (PonyoNIApp.exe ${EXTRA_LIBS})



# LIB - Boost
##############################################################

set(BOOST_LIBS thread date_time system)
find_package(Boost COMPONENTS ${BOOST_LIBS} REQUIRED)
target_link_libraries(PonyoNIApp.exe ${Boost_LIBRARIES})
find_package(Threads REQUIRED)
target_link_libraries(PonyoNIApp.exe ${CMAKE_THREAD_LIBS_INIT})
