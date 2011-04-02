#
# make [install]:
# * compiles JavaApp.java to target/java
# * creates src/cpp/jniplay_JavaApp.h
# * compiles cpp to target/libjniPlayLib-VERSION.jnilib
# * moves jnilib to java extensions folder
# 
# make run:
# ... just as make install, plus additional runs java class
#

COMPILER = g++

# CONFIG - platform dependent
CPP_INC_JNI = /Developer/SDKs/MacOSX10.6.sdk/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Headers
PATH_JAVA_EXTENSIONS = /Library/Java/Extensions

# CONFIG - project related
VERSION = 0.1-SNAPSHOT
PATH_SRC = src
PATH_SRC_CPP = $(PATH_SRC)/cpp
PATH_SRC_JAVA = $(PATH_SRC)/java
PATH_TARGET = target
PATH_TARGET_JAVA = $(PATH_TARGET)/java
PATH_FINAL_BUILD_ARTIFACT = $(PATH_TARGET)/libjniPlayLib-$(VERSION).jnilib

JAVA_FQN = jniplay.JavaApp

# C_FLAGS
# L_FLAGS

run: install
	-@echo
	-@echo "================================================="
	-@echo "               Running Java class                "
	-@echo "================================================="
	java -classpath $(PATH_TARGET_JAVA) $(JAVA_FQN)

install: clean init make_jni_header make_native_lib
	-@echo
	-@echo "================================================="
	-@echo "                 Installing Lib                  "
	-@echo "================================================="
	cp $(PATH_FINAL_BUILD_ARTIFACT) $(PATH_JAVA_EXTENSIONS)

make_native_lib:
	-@echo
	-@echo "================================================="
	-@echo "               Building Native Lib               "
	-@echo "================================================="
	$(COMPILER) \
		-dynamiclib \
		-o $(PATH_FINAL_BUILD_ARTIFACT) \
		-I$(CPP_INC_JNI) \
		-framework JavaVM \
		$(PATH_SRC_CPP)/jniPlayLib.cpp

make_jni_header: make_java_class
	-@echo
	-@echo "================================================="
	-@echo "                  Building JNI                   "
	-@echo "================================================="
	javah \
		-d $(PATH_SRC_CPP) \
		-jni \
		-classpath $(PATH_TARGET_JAVA) \
		$(JAVA_FQN)

make_java_class:
	-@echo
	-@echo "================================================="
	-@echo "                 Compiling Java                  "
	-@echo "================================================="
	javac \
		-d $(PATH_TARGET_JAVA) \
		$(PATH_SRC_JAVA)/jniplay/JavaApp.java

init:
	-@echo
	-@echo "================================================="
	-@echo "                  Initializing                   "
	-@echo "================================================="
	mkdir $(PATH_TARGET)
	mkdir $(PATH_TARGET_JAVA)
	
clean:
	-@echo
	-@echo "================================================="
	-@echo "                    Cleaning                     "
	-@echo "================================================="
	rm -rf $(PATH_TARGET)

.PHONY: install

# statically link libs!
# gcc -static -L../common-dir ftp.o -lcommon -o ft