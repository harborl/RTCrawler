#!/bin/sh

set -h -e

CLASSPATH=.:lib/*
java -server \
     -Xms512M -Xmx1024M -Xss256k -XX:PermSize=256m -XX:MaxPermSize=256m \
     -cp $CLASSPATH \
     com.homethy.crawler.coldwell.clts.Crawler $@