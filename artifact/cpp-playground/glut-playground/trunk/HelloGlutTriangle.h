#ifndef HELLOGLUTTRIANGLE_H_
#define HELLOGLUTTRIANGLE_H_

class HelloGlutTriangle {
public:
	HelloGlutTriangle();
	virtual ~HelloGlutTriangle();

	void display(int, char**);

private:
	static /*glut callback*/ void renderScene();

};

#endif /* HELLOGLUTTRIANGLE_H_ */
