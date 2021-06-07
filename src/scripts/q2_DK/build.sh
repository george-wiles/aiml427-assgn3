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

rm -r ${class_dir}_lr3
mkdir ${class_dir}_lr3
rm bdce1_lr3.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_lr3 $src_dir/q2_DK/BigDataClusterEater3.java
jar cvf bdce1_lr3.jar -C ${class_dir}_lr3/ .

rm -r ${class_dir}_lr4
mkdir ${class_dir}_lr4
rm bdce1_lr4.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_lr4 $src_dir/q2_DK/BigDataClusterEater4.java
jar cvf bdce1_lr4.jar -C ${class_dir}_lr4/ .

rm -r ${class_dir}_lr5
mkdir ${class_dir}_lr5
rm bdce1_lr5.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_lr5 $src_dir/q2_DK/BigDataClusterEater5.java
jar cvf bdce1_lr5.jar -C ${class_dir}_lr5/ .

rm -r ${class_dir}_lr6
mkdir ${class_dir}_lr6
rm bdce1_lr6.jar
javac -cp "$lib_dir/jars/*" -d ${class_dir}_lr6 $src_dir/q2_DK/BigDataClusterEater6.java
jar cvf bdce1_lr6.jar -C ${class_dir}_lr6/ .
