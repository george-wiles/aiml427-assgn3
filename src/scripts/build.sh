#!/usr/bin/env bash

class_dir = skdd_classes
resources_dir = ../main/resources


rm -r $class_dir
mkdir $class_dir
rm skddlr.jar
javac -cp "../../../lib/jars/*" -d skdd_classes SparkKDDLoadTest.java
jar cvf skddlr2.jar -C skdd_classes/ .




