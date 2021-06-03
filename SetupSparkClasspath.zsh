export HADOOP_VERSION=2.8.0
export HADOOP_PREFIX=/local/Hadoop/hadoop-$HADOOP_VERSION
export SPARK_HOME=/local/spark/
export PATH=${PATH}:$HADOOP_PREFIX/bin:$SPARK_HOME/bin
export HADOOP_CONF_DIR=$HADOOP_PREFIX/etc/hadoop
export YARN_CONF_DIR=$HADOOP_PREFIX/etc/hadoop
export LD_LIBRARY_PATH=$HADOOP_PREFIX/lib/native:$JAVA_HOME/jre/lib/amd64/server
need java8
