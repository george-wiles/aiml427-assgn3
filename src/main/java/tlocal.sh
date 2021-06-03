#!/usr/bin/env bash

spark-submit --class SparkKDDLoadTest --master local skddlr2.jar resources/kdd.data
