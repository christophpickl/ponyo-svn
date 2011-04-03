#pragma once
#ifndef LOG_H_
#define LOG_H_

class Log {
public:
	Log(const char*);
	virtual ~Log();
	void debug(const char*);

private:
	const char* sourceFile;

};

#endif // LOG_H_
