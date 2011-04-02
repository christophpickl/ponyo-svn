#ifndef HELLOGLUTTRIANGLE_H_
#define HELLOGLUTTRIANGLE_H_

class HelloGlutTriangle {
public:
	HelloGlutTriangle();
	virtual ~HelloGlutTriangle();

	void display(int, char**);

	static float angle; // TODO static ;(
private:

	static /*glut callback*/ void renderScene();
	static /*glut callback*/ void changeSize(int, int);

};

#endif /* HELLOGLUTTRIANGLE_H_ */
