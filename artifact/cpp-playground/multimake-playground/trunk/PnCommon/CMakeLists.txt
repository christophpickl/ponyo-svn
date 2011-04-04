message("==============> PnCommon")

set (PROJECT_SOURCE_DIR src)

set (PNCOMMON_VERSION \"0.1-alpha-SNAPSHOT\")
configure_file (
  "${PROJECT_SOURCE_DIR}/PnCommonVersion.hpp.in"
  "${PROJECT_SOURCE_DIR}/PnCommonVersion.hpp"
)

add_library (PnCommon ${PROJECT_SOURCE_DIR}/MyCommon.cpp)
