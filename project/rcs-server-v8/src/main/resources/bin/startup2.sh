#!/bin/bash
export JAVA_HOME=/home/program/java/jdk1.8.0_60              ###jdk安装路径
java=$JAVA_HOME/bin/java

JAVA_OPTS=$JAVA_OPTS:"-Xms1024m -Xmx1024m"
export JAVA_OPTS
SERVER_HOME=/opt/nutch                     ###放置lib文件夹和shell的目录
LIBDIR=$SERVER_HOME/lib

CONFDIR=$SERVER_HOME/conf                  ###可去掉，放置配置文件
export CONFDIR

export LIBDIR
CLASSPATH=${CLASSPATH}:${LIBDIR}
CLASSPATH=${CLASSPATH}:${CONFDIR}:${LIBDIR}


for cdir in ${LIBDIR}/*.jar
do
 CLASSPATH=$CLASSPATH:$cdir
 export CLASSPATH
done
export CLASSPATH
exec $java com.yaochufa.jframework.nutch.AppStarter $* >logs/stdout.log 2>&1 &                   ###指定启动的程序类

#tail -f /home/yang/program/file/stock_task/logs/stdout.log