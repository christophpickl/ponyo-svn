CMAKE_MINIMUM_REQUIRED (VERSION 2.8)
SET (PROJECT_SOURCE_DIR oscpack)
INCLUDE_DIRECTORIES (${PROJECT_SOURCE_DIR})
#INCLUDE_DIRECTORIES (${CMAKE_SOURCE_DIR})
SET (MY_LIB_NAME oscpack)

IF(WIN32)
 set(SystemTypePath ip/win32)
ELSE(WIN32)
 set(SystemTypePath ip/posix)
ENDIF(WIN32)

SET (LIB_SOURCES
	${PROJECT_SOURCE_DIR}/ip/IpEndpointName.cpp 
	${PROJECT_SOURCE_DIR}/${SystemTypePath}/NetworkingUtils.cpp
	${PROJECT_SOURCE_DIR}/${SystemTypePath}/UdpSocket.cpp
	${PROJECT_SOURCE_DIR}/osc/OscPrintReceivedElements.cpp
	${PROJECT_SOURCE_DIR}/osc/OscReceivedElements.cpp 
	${PROJECT_SOURCE_DIR}/osc/OscTypes.cpp 
	${PROJECT_SOURCE_DIR}/osc/OscOutboundPacketStream.cpp)
ADD_LIBRARY(${MY_LIB_NAME} SHARED ${LIB_SOURCES})

#IF(WIN32)
# ADD_EXECUTABLE(OscDump examples/OscDump.cpp)
# TARGET_LINK_LIBRARIES(OscDump ${MY_LIB_NAME} Ws2_32 winmm)
# ADD_EXECUTABLE(SimpleReceive examples/SimpleReceive.cpp)
# TARGET_LINK_LIBRARIES(SimpleReceive ${MY_LIB_NAME} Ws2_32 winmm)
# ADD_EXECUTABLE(SimpleSend examples/SimpleSend.cpp)
# TARGET_LINK_LIBRARIES(SimpleSend ${MY_LIB_NAME} Ws2_32 winmm)
#ELSE(WIN32)
# ADD_EXECUTABLE(OscDump examples/OscDump.cpp)
# TARGET_LINK_LIBRARIES(OscDump ${MY_LIB_NAME})
# ADD_EXECUTABLE(SimpleReceive examples/SimpleReceive.cpp)
# TARGET_LINK_LIBRARIES(SimpleReceive ${MY_LIB_NAME})
# ADD_EXECUTABLE(SimpleSend examples/SimpleSend.cpp)
# TARGET_LINK_LIBRARIES(SimpleSend ${MY_LIB_NAME})
#ENDIF(WIN32)


INSTALL (TARGETS ${MY_LIB_NAME} LIBRARY DESTINATION lib)
INSTALL (DIRECTORY ${PROJECT_SOURCE_DIR}
         DESTINATION include
         FILES_MATCHING PATTERN "*.h"
                        PATTERN ".svn" EXCLUDE
        )