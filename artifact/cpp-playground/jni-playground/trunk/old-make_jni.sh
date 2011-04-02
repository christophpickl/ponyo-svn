#!/bin/bash

# http://pujansrt.blogspot.com/2010/04/creating-jni-library-on-mac-osx-from-c.html
	# statt *.jnilib auch *.dylib moeglich
# auch gutes tutorial: http://www.cs.fit.edu/~ryan/java/language/jni.html
# http://www.codetoad.com/java_simplejni.asp

#gcc -c -I/Developer/SDKs/MacOSX10.6.sdk/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0 myjni_MyJniApp.c
# gcc -dynamiclib -o libhello.jnilib -framework JavaVM

#gcc -c \
#	-I/Developer/SDKs/MacOSX10.6.sdk/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Headers \
#	-c myjni_MyJniApp.c \
#	-o MyJniApp.o
# ld -G MyJniApp.o.o -o libFOOOOOBAR.jnilib

gcc -dynamiclib -o libfirstjni.jnilib \
	-I/Developer/SDKs/MacOSX10.6.sdk/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Headers \
	myjni_MyJniApp.c -framework JavaVM


# gcc  -o libfirstjni.jnilib -shared -Wl,-soname,libfirstjni.jnilib \
# -I$(JAVA_HOME)/include \
# -I$(JAVA_HOME)/linux myjni_MyJniApp.c \
# -static -lc