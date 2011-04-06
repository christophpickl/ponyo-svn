message("")
message("==============> PnOpenNI")

set (PROJECT_SOURCE_DIR src)

file(GLOB_RECURSE CPP_FILES "${PROJECT_SOURCE_DIR}/*.cpp")
message("CPP_FILES: ${CPP_FILES}")
add_library(PnOpenNI ${CPP_FILES})

