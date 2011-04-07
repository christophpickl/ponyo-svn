#pragma once
#ifndef MAINWINDOWLISTENER_HPP_
#define MAINWINDOWLISTENER_HPP_

namespace pn {
class MainWindowListener {
public:
	virtual void onListDevices() = 0;
	virtual void onStartGenerating() = 0;
	virtual void onCalibrate() = 0;
	virtual void onQuit() = 0;
};
}

#endif // MAINWINDOWLISTENER_HPP_
