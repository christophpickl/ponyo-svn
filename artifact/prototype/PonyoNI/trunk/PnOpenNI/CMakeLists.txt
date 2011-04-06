message("")
message("==============> PnOpenNI")

set (PROJECT_SOURCE_DIR src)

#set(EXTRA_LIBS ${EXTRA_LIBS} PnOpenNI)
message("EXTRA_LIBS: ${EXTRA_LIBS}")

file(GLOB_RECURSE CPP_FILES "${PROJECT_SOURCE_DIR}/*.cpp")
message("CPP_FILES: ${CPP_FILES}")
add_library(PnOpenNI ${CPP_FILES})

