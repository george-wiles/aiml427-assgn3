#!/usr/bin/env bash

root_dir="/home/wilesgeor/dev/git/aiml427-assgn3"
script_dir=${root_dir}"/src/scripts"
resources_dir=${root_dir}"/src/main/resources"
lib_dir=${root_dir}"/lib"
src_dir=${root_dir}"/src/main/java"
class_dir=${script_dir}"/q1/skdd_classes"

rm -r ${class_dir}_lr
mkdir ${class_dir}_lr
rm ${script_dir}/q1/skdd_lr.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_lr $src_dir/q1/pipeline_SparkLoadLinearRegression.java
jar cvf ${script_dir}/q1/skdd_lr.jar -C ${class_dir}_lr/ .
rm -r ${class_dir}_lr
mkdir ${class_dir}_lr

rm -r ${class_dir}_dt
mkdir ${class_dir}_dt
rm ${script_dir}/q1/skdd_dt.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_dt $src_dir/q1/pipeline_SparkLoadDecisionTree.java
jar cvf skdd_dt.jar -C ${class_dir}_dt/ .
rm -r ${class_dir}_dt
mkdir ${class_dir}_dt


