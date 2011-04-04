message("==============> ROOT")

cmake_minimum_required (VERSION 2.8) 
project (PONYO)


##############################################################
# PnCommon
##############################################################
include_directories ("${PROJECT_SOURCE_DIR}/PnCommon/src")
add_subdirectory (PnCommon)
set (EXTRA_LIBS ${EXTRA_LIBS} PnCommon)


##############################################################
# PnFooExe
##############################################################
add_subdirectory (PnFooExe)
add_executable (pnFoo.exe ${PROJECT_SOURCE_DIR}/PnFooExe/src/app.cpp)
target_link_libraries (pnFoo.exe ${EXTRA_LIBS})
