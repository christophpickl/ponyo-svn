message("==============> ROOT")

cmake_minimum_required (VERSION 2.8) 
project (PONYO)



add_subdirectory (PnFooExe)


include_directories ("${PROJECT_SOURCE_DIR}/PnCommon/src")
add_subdirectory (PnCommon)
set (EXTRA_LIBS ${EXTRA_LIBS} PnCommon)

add_executable (pnFoo.exe ${PROJECT_SOURCE_DIR}/PnFooExe/src/app.cpp)
target_link_libraries (pnFoo.exe ${EXTRA_LIBS})

#add_executable (pnFoo.exe ${PROJECT_SOURCE_DIR}/app.cpp)
#add_executable (Tutorial tutorial.cxx)
