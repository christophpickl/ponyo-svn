#!/bin/bash

javac -classpath ".:jna.jar:platform.jar" pnj/Japp.java
java -Djna.library.path="../native/jna" -classpath "./:jna.jar:platform.jar" pnj.Japp

#javac -classpath ".:jna.jar:platform.jar" foo/HelloWorld.java
#java -classpath "./:jna.jar:platform.jar" foo.HelloWorld