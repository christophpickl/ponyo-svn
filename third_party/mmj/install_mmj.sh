#!/bin/sh

mvn install:install-file -Dfile=mmj.jar \
                         -DgroupId=de.humatic \
                         -DartifactId=mmj \
                         -Dversion=0_93-ponyo \
                         -Dpackaging=jar \
                         -DgeneratePom=true
