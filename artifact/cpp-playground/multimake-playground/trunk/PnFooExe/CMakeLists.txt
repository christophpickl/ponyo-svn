message("==============> PnFooExe")

project (PnFooExe)
set (PROJECT_SOURCE_DIR src)
message("PROJECT_SOURCE_DIR = ${PROJECT_SOURCE_DIR}")

set (PNFOOEXE_VERSION \"0.1-SNAPSHOT\")

# configure a header file to pass some of the CMake settings to the source code
configure_file (
  "${PROJECT_SOURCE_DIR}/app.hpp.in"
  "${PROJECT_SOURCE_DIR}/app.hpp"
#  "${PROJECT_BINARY_DIR}/app.hpp"
)

# add the binary tree to the search path for include files so that we will find app.hpp
include_directories("${PROJECT_BINARY_DIR}")


#add_executable (pnFoo.exe ${PROJECT_SOURCE_DIR}/app.cpp)