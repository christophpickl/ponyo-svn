#pragma once
#ifndef MAINWINDOW_HPP_
#define MAINWINDOW_HPP_

#include <ponyo/pncommon/log/Log.hpp>
#include <ponyo/pncommon/Async.hpp>

#include "MainWindowListener.hpp"

namespace pn {

class MainWindow : public Async<MainWindowListener*> {
public:
	static MainWindow* singleton; // static callback hack

	MainWindow();
	virtual ~MainWindow();

	void init(int, char**);
	void display();

private:
	static Log* LOG;
	int mainWindowHandler;

	void initContextMenu();

	void onHandleLoadDevices();
	void onHandleStartGenerating();
	void onHandleCalibrate();
	void onHandleQuit();

//	static void onGlutMenuStatus(int, int, int);
	static void onGlutKeyboard(unsigned char, int, int);
	static void onMenuItemClicked(int);
	static void onGlutDisplay();

};
}

#endif // MAINWINDOW_HPP_
