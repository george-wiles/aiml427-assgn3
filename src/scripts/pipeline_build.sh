#!/usr/bin/env bash

class_dir="skdd_classes"
resources_dir="../main/resources"
lib_dir="../../lib"
src_dir="../main/java"

rm -r ${class_dir}_pllr
mkdir ${class_dir}_pllr
rm skdd_pllr.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_pllr $src_dir/q1/pipeline_SparkLoadLinearRegression.java
jar cvf skdd_pllr.jar -C ${class_dir}_pllr/ .

rm -r ${class_dir}_pldt
mkdir ${class_dir}_pldt
rm skdd_pldt.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_pldt $src_dir/q1/pipeline_SparkLoadDecisionTree.java
jar cvf skdd_pldt.jar -C ${class_dir}_pldt/ .

