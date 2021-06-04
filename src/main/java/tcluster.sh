#!/usr/bin/env bash

spark-submit --class SparkKDDLoadTest --master yarn --deploy-mode cluster skddlr2.jar resources/kdd.data
