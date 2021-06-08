#!/usr/bin/env bash

root_dir="/home/wilesgeor/dev/git/aiml427-assgn3"
script_dir=${root_dir}"/src/scripts"
resources_dir=${root_dir}"/src/main/resources"
lib_dir=${root_dir}"/lib"
src_dir=${root_dir}"/src/main/java"
class_dir=${script_dir}"/q2/skdd_classes"

rm -r ${class_dir}_lr_pca
mkdir ${class_dir}_lr_pca
rm ${script_dir}/q2/skdd_lr_pca.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_lr_pca $src_dir/q2/SparkLoadLinearRegressionAIGNewsWithPCA.java
jar cvf ${script_dir}/q2/skdd_lr_pca.jar -C ${class_dir}_lr_pca/ .

rm -r ${class_dir}_dt_pca
mkdir ${class_dir}_dt_pca
rm ${script_dir}/q2/skdd_dt_pca.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_dt_pca $src_dir/q2/SparkLoadDecisionTreeAIGNewsWithPCA.java
jar cvf ${script_dir}/q2/skdd_dt_pca.jar -C ${class_dir}_dt_pca/ .
