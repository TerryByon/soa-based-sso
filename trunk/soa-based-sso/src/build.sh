#!/bin/sh

export JAVA_HOME=/usr/local/java
export ANT_HOME=/home/deploy/apache-ant-1.7.0
export PATH=$PATH:$JAVA_HOME/bin:$ANT_HOME/bin

ant -buildfile /home/deploy/sso/build.xml

exit 0