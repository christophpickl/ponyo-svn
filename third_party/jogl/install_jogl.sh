#!/bin/sh

mvn install:install-file -Dfile=jogl.jar \
                         -DgroupId=net.java.dev.jogl \
                         -DartifactId=jogl \
                         -Dversion=1.1.1a-ponyo \
                         -Dpackaging=jar \
                         -DgeneratePom=true

mvn install:install-file -Dfile=gluegen-rt.jar \
                         -DgroupId=net.java.dev.gluegen \
                         -DartifactId=gluegen-rt \
                         -Dversion=1.1.1a-ponyo \
                         -Dpackaging=jar \
                         -DgeneratePom=true
