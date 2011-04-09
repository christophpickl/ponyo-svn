cmake_minimum_required (VERSION 2.8) 
project (ponyoni_prototype_playground)

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g")
SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2")

INCLUDE_DIRECTORIES( /usr/include/ponyo )
#INCLUDE_DIRECTORIES( /usr/include/ponyo/pncommon )


#FIND_LIBRARY(PnCommon_LIBRARY PnCommon)
#SET(EXTRA_LIBS ${EXTRA_LIBS} ${PnCommon_LIBRARY} )

FIND_LIBRARY(PxCommon_LIBRARY PxCommon)
SET(EXTRA_LIBS ${EXTRA_LIBS} ${PxCommon_LIBRARY} )



ADD_EXECUTABLE (App App.cpp)
TARGET_LINK_LIBRARIES (App ${EXTRA_LIBS})
