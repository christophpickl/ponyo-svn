message("==============> ROOT")

cmake_minimum_required (VERSION 2.8) 
project (PonyoNI)


##############################################################
# PnCommon
##############################################################

include_directories("${PROJECT_SOURCE_DIR}/PnCommon/src")
add_subdirectory(PnCommon)
set(EXTRA_LIBS ${EXTRA_LIBS} PnCommon)


##############################################################
# PonyoNIApp
##############################################################

add_subdirectory(PonyoNIApp)
add_executable (PonyoNIApp.exe ${PROJECT_SOURCE_DIR}/PonyoNIApp/src/app.cpp)
target_link_libraries (PonyoNIApp.exe ${EXTRA_LIBS})
