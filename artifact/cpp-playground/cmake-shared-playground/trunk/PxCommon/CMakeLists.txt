message("")
message("==============> PxCommon")

set (PROJECT_SOURCE_DIR pxcommon)

file(GLOB_RECURSE PXCOMMON_CPP_FILES "${PROJECT_SOURCE_DIR}/*.cpp")
add_library(PxCommon SHARED ${PXCOMMON_CPP_FILES})

install(TARGETS PxCommon LIBRARY DESTINATION lib)

install(DIRECTORY pxcommon
        DESTINATION include/ponyo
        FILES_MATCHING PATTERN "*.hpp"
                       PATTERN ".svn" EXCLUDE
       )