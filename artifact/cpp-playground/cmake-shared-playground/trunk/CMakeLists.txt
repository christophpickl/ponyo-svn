# general setup
	cmake_minimum_required (VERSION 2.8) 
	project (PxShared)

	#SET(TARGET_FOLDER ${PROJECT_SOURCE_DIR}/target)
	#SET(LIBRARY_OUTPUT_PATH ${TARGET_FOLDER})

	SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g")
	SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2")


# LIB - OpenNI
	FIND_LIBRARY(OpenNI_LIBRARY OpenNI )
	SET(EXTRA_LIBS ${EXTRA_LIBS} ${OpenNI_LIBRARY} )
	INCLUDE_DIRECTORIES( /usr/include/ni )

# PxCommon
	add_subdirectory(PxCommon)
	include_directories("${PROJECT_SOURCE_DIR}/PxCommon/pxcommon")
	set(EXTRA_LIBS ${EXTRA_LIBS} PxCommon)

# PxApp
	add_subdirectory(PxApp)

##############################################################

SET (PROGRAMS ${PROGRAMS} PxApp)
FOREACH (PROGRAM ${PROGRAMS})
  SET (PROGRAM_SRC_FOLDER "${PROJECT_SOURCE_DIR}/${PROGRAM}/src")
  FILE (GLOB_RECURSE PROGRAM_CPP_FILES "${PROGRAM_SRC_FOLDER}/*.cpp")
  ADD_EXECUTABLE (${PROGRAM}Exe ${PROGRAM_CPP_FILES})
  TARGET_LINK_LIBRARIES (${PROGRAM}Exe ${EXTRA_LIBS})
  #install(TARGETS ${PROGRAM}Exe RUNTIME DESTINATION somebin)
ENDFOREACH (PROGRAM)

#add_dependencies(PxApp PxCommon)

message("[ROOT] EXTRA_LIBS: ${EXTRA_LIBS}")
message("[ROOT] CMAKE_CXX_FLAGS: ${CMAKE_CXX_FLAGS}")
