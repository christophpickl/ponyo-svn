#pragma once
#ifndef HEADERS_GLUT_HPP_
#define HEADERS_GLUT_HPP_

//#ifdef PN_LOCAL_IDE_GLUT_HACK
//	#include <OpenGL.framework/Headers/gl.h>
//	#include <OpenGL.framework/Headers/glu.h>
//	#include <GLUT.framework/Headers/glut.h>
//#elif defined(__APPLE__) && defined(__MACH__)
	#include <GLUT/glut.h>
//#else
//	# include <GL/glut.h>
//#endif


//	OpenGL == cross-platform, right? Almost. Porting an existing OpenGL application to or from OS X is largely a matter of headers (I think...).
//	For some unknown reason, OS X puts the headers in a different location in the include path than Windows and Linux (and probably a bunch of other *n?x OSes).
//	Here's the skinny:
//
//	Header Description 	Mac OS X 	The Rest of the World
//	GL - OpenGL Base 	#include <OpenGL/gl.h> 	#include <GL/gl.h>
//	GLU - OpenGL Utility 	#include <OpenGL/glu.h> 	#include <GL/glu.h>
//	GLUT - OpenGL Utility Toolkit 	#include <GLUT/glut.h> 	#include <GL/glut.h>
//
//	see: http://alumni.cs.ucsb.edu/~wombatty/tutorials/opengl_mac_osx.html

#endif // HEADERS_GLUT_HPP_
