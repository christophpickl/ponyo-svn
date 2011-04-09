cmake_minimum_required (VERSION 2.8) 
project (ponyoni_prototype_playground)

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g")
SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2")

FIND_LIBRARY(PnCommon_LIBRARY PnCommon)
SET(EXTRA_LIBS ${EXTRA_LIBS} ${PnCommon_LIBRARY} )
INCLUDE_DIRECTORIES( /usr/include/ponyo/pncommon )

ADD_EXECUTABLE (App App.cpp)
TARGET_LINK_LIBRARIES (App ${EXTRA_LIBS})
