
#ifndef PONYO_COMMON_H_
#define PONYO_COMMON_H_

#include <string>
#include <XnCppWrapper.h>

// TODO throw an exception with ERROR CODE only (map to java afterwards with proper message!)
#define THROW_XN_EXCEPTION(errorMessage, returnCode) \
std::string ss;                                      \
ss.append(errorMessage);                             \
ss.append(" - ");                                    \
ss.append(xnGetStatusString(returnCode));            \
throw ss;


#endif /* PONYO_COMMON_H_ */
