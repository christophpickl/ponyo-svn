
#ifndef KINECTWINDOW_H_
#define KINECTWINDOW_H_

class KinectWindow {
public:
	KinectWindow();
	virtual ~KinectWindow();

	void display(int, char**);

private:
	static /*glut callback*/ void renderScene();

	static void glutKeyboard(unsigned char, int, int);
	static void glutDisplay();
	static void glutIdle();
};

#endif /* KINECTWINDOW_H_ */
