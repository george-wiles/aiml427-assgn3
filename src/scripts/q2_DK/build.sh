#!/usr/bin/env bash

class_dir="my_classes"
lib_dir="../../../lib"
src_dir="../../main/java"

rm -r ${class_dir}_lr
mkdir ${class_dir}_lr
rm bdce1_lr.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_lr $src_dir/q2_DK/BigDataClusterEater1.java
jar cvf bdce1_lr.jar -C ${class_dir}_lr/ .

rm -r ${class_dir}_lr2
mkdir ${class_dir}_lr2
rm bdce1_lr2.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_lr2 $src_dir/q2_DK/BigDataClusterEater2.java
jar cvf bdce1_lr2.jar -C ${class_dir}_lr2/ .

