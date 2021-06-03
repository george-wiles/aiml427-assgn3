#!/usr/bin/env bash

rm -r skdd_classes
mkdir skdd_classes
javac -cp "../../../lib/jars/*" -d skdd_classes SparkKDDLoadTest.java
jar cvf skddlr.jar -C skdd_classes/ .




