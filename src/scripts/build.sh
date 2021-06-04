#!/usr/bin/env bash

class_dir="skdd_classes"
resources_dir="../main/resources"
lib_dir="../../lib"
src_dir="../main/java"


rm -r ${class_dir}_lr
mkdir ${class_dir}_lr
rm skdd_lr.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_lr $src_dir/q1/SparkLoadLinearRegression.java
jar cvf skdd_lr.jar -C ${class_dir}_lr/ .
rm -r ${class_dir}_dt
mkdir ${class_dir}_dt

rm -r ${class_dir}_dt
mkdir ${class_dir}_dt
rm skdd_dt.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_dt $src_dir/q1/SparkLoadDecisionTree.java
jar cvf skdd_dt.jar -C ${class_dir}_dt/ .
rm -r ${class_dir}_dt
mkdir ${class_dir}_dt


