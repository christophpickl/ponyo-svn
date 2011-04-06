message("==============> ROOT")

cmake_minimum_required (VERSION 2.8) 
project (PonyoNI)


#find_package(GLUT)
#find_package(OpenGL)

set(CMAKE_CXX_FLAGS "-g -Wall -O2")


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
add_executable (PonyoNIApp.exe ${PROJECT_SOURCE_DIR}/PonyoNIApp/src/app.cpp)
target_link_libraries (PonyoNIApp.exe ${EXTRA_LIBS})
