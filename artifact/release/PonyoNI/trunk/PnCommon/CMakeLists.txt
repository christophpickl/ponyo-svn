PN_DEPENDENCY_OPENNI ()
PN_DEPENDENCY_BOOST ()

PN_INIT_LIB (PnCommon)

configure_file (
  "${PROJECT_SOURCE_DIR}/ponyo/common/pnfiltering.hpp.in"
  "${PROJECT_SOURCE_DIR}/ponyo/common/pnfiltering.hpp"
)
