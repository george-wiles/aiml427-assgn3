#!/usr/bin/env bash

rm -r skdd_classes
mkdir skdd_classes
rm skddlr2.jar
javac -cp "../../../lib/jars/*" -d skdd_classes SparkKDDLoadTest.java
jar cvf skddlr2.jar -C skdd_classes/ .




