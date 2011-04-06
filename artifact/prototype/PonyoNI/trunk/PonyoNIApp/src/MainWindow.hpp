#pragma once
#ifndef MAINWINDOW_HPP_
#define MAINWINDOW_HPP_

namespace pn {
class MainWindow {
public:
	MainWindow();
	virtual ~MainWindow();

	void startInfiniteLoop(int, char**);

private:
	static void onGlutKeyboard(unsigned char, int, int);
	static void onGlutDisplay();

};
}

#endif // MAINWINDOW_HPP_
