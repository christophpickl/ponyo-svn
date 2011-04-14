#pragma once
#ifndef FILENOTFOUNDEXCEPTION_HPP_
#define FILENOTFOUNDEXCEPTION_HPP_

#include <ponyo/common/exception/Exception.hpp>

namespace pn {

/**
 * @version 0.1
 */
class FileNotFoundException : public Exception {
public:
	FileNotFoundException(const char* notFoundFile, const char* sourceFile, int sourceLine);
	virtual ~FileNotFoundException();
private:
	static Log* LOG;
};
}

#endif // FILENOTFOUNDEXCEPTION_HPP_
