#pragma once
#ifndef CHECKXN_INC_HPP_
#define CHECKXN_INC_HPP_

#define THROW_XN_EXCEPTION(xnStatus, customErrorMessage)                 \
	std::string ss;                                                      \
	ss.append(customErrorMessage);                                       \
	ss.append(" (OpenNI message: ");                                     \
	ss.append(xnGetStatusString(xnStatus));                              \
	ss.append(")");                                                      \
	throw OpenNiException(ss.c_str(), AT);
// TODO enable exception output print by default?! std::cerr << "Throwing OpenNiException: " << ss << std::endl;

#define CHECK_XN(xnStatus, customErrorMessage) \
		if(xnStatus != XN_STATUS_OK) { THROW_XN_EXCEPTION(xnStatus, customErrorMessage); }


#endif // CHECKXN_INC_HPP_
