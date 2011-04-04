message("==============> PnFooExe")

project (PnFooExe)
set (PROJECT_SOURCE_DIR src)

set (PNFOOEXE_VERSION \"0.1-SNAPSHOT\")
configure_file (
  "${PROJECT_SOURCE_DIR}/app.hpp.in"
  "${PROJECT_SOURCE_DIR}/app.hpp"
#  "${PROJECT_BINARY_DIR}/app.hpp"
)

# add the binary tree to the search path for include files so that we will find app.hpp
include_directories("${PROJECT_BINARY_DIR}")
