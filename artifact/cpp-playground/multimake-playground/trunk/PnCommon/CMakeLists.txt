message("==============> PnCommon")

set (PROJECT_SOURCE_DIR src)
message("PROJECT_SOURCE_DIR = ${PROJECT_SOURCE_DIR}")

add_library (PnCommon ${PROJECT_SOURCE_DIR}/MyCommon.cpp)
