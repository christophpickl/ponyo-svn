#pragma once
#ifndef LOG_HPP_
#define LOG_HPP_

// Log* Foobar::LOG = LogFactory::getLog(__FILE__);

class Log {
public:
	Log(const char*);
	virtual ~Log();

	void error(const char*);
	void info(const char*);
	void debug(const char*);
	void trace(const char*);

private:
	const char* sourceFile;
};

#endif // LOG_HPP_
