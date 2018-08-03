#!/bin/sh
#该脚本为Linux下启动java程序的脚本。即可以作为开机自启动service脚本被调用，
#也可以作为启动java程序的独立脚本来使用。
#警告!!!：该脚本stop部分使用系统kill命令来强制终止指定的java程序进程。
#在杀死进程前，未作任何条件检查。在某些情况下，如程序正在进行文件或数据库写操作，
#可能会造成数据丢失或数据不完整。如果必须要考虑到这类情况，则需要改写此脚本，
#增加在执行kill命令前的一系列检查。
###################################
#环境变量及程序执行参数
#需要根据实际环境以及Java程序名称来修改这些参数
###################################
#JDK所在路径
export JAVA_HOME=/usr/java/jdk1.8
export CLASSPATH=.:$JAVA_HOME/lib:$CLASSPATH
export PATH=.:$JAVA_HOME/bin:$PATH


#脚本所在的目录
RCS_HOME=$(cd "$(/home/mushiny/RCS/ "$0")"; pwd)


#class名称，编译时替换
RCS_CLASS=com.mushiny.kivaconfigtool.KivaConfigTool


#jar名称，编译时替换
RCS_LIB=KivaConfigTool-1.0-SNAPSHOT.jar


#日志
RCS_LOG=$APP_HOME/log


#java虚拟机启动参数
JAVA_OPTS="-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"




###################################
#(函数)判断程序是否已启动
#说明：
#使用JDK自带的JPS命令及grep命令组合，准确查找pid
#jps 加 l 参数，表示显示java的完整包路径
#使用awk，分割出pid ($1部分)，及Java程序名称($2部分)
###################################
#初始化psid变量（全局）
psid=0
checkpid() {
javaps=`jps -l|grep $RCS_CLASS`
if [ -n "$javaps" ]; then
psid=`echo $javaps | awk '{print $1}'`
else
psid=0
fi
}


###################################
#(函数)启动程序
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则提示程序已启动
#3. 如果程序没有被启动，则执行启动命令行
#4. 启动命令执行后，再次调用checkpid函数
#5. 如果步骤4的结果能够确认程序的pid,则打印[OK]，否则打印[Failed]
#注意：echo -n 表示打印字符后，不换行
#注意: "nohup 某命令 &gt;/dev/null 2&gt;&amp;1 &amp;" 的用法
###################################






start() {
echo "****************************"
checkpid
if [ $psid -ne 0 ]; then
echo "================================"
echo "程序: $RCS_CLASS 已经启动! (进程号=$psid)"
echo "================================"
else
echo "启动 $RCS_CLASS ..."
nohup java $JAVA_OPTS -Djava.ext.dirs=$RCS_LIB $RCS_CLASS >> nohup.out 2>&1 &
checkpid
if [ $psid -ne 0 ]; then
echo "程序: $RCS_CLASS 启动成功! (进程号=$psid) [OK]"
else
echo "程序: $RCS_CLASS 启动失败! [Failed]"
fi
fi
echo "****************************"
}



###################################
#(函数)停止程序
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则开始执行停止，否则，提示程序未运行
#3. 使用kill -2 pid命令进行强制杀死进程
#4. 执行kill命令行紧接其后，马上查看上一句命令的返回值: $?
#5. 如果步骤4的结果$?等于0,则打印[OK]，否则打印[Failed]
#6. 为了防止java程序被启动多次，这里增加反复检查进程，反复杀死的处理（递归调用stop）。
#注意：echo -n 表示打印字符后，不换行
#注意: 在shell编程中，"$?" 表示上一句命令或者一个函数的返回值
###################################


stop() {
echo "****************************"
checkpid
if [ $psid -ne 0 ]; then
echo "正在停止 $RCS_CLASS ...(pid=$psid)"
kill -2 $psid
if [ $? -eq 0 ]; then
echo "停止 $RCS_CLASS 成功!(pid=$psid) [OK]"
else
echo "正在停止 $RCS_CLASS 失败!(pid=$psid) [Failed]"
fi


sleep 2
checkpid
if [ $psid -ne 0 ]; then
kill -9 $psid
fi
else
echo "warn: $RCS_CLASS 没有运行..."
fi
echo "****************************"
}
###################################
#(函数)检查程序运行状态
#
#说明：
#1. 首先调用checkpid函数，刷新$psid全局变量
#2. 如果程序已经启动（$psid不等于0），则提示正在运行并表示出pid
#3. 否则，提示程序未运行
###################################


status() {
echo "****************************"
checkpid
if [ $psid -ne 0 ]; then
echo "$RCS_CLASS 正在运行! (进程号=$psid)"
else
echo "$RCS_CLASS 没有运行"
fi
echo "****************************"
}
###################################
#(函数)打印系统环境参数
###################################
info() {
echo "System Information:"
echo "****************************"
echo `head -n 1 /somnus/issue`
echo `uname -a`
echo
echo "JAVA_HOME=$JAVA_HOME"
echo `java -version`
echo
echo "RCS_HOME=$RCS_HOME"
echo "RCS_CLASS=$RCS_CLASS"
echo "****************************"
}
###################################
#(函数)打印log
###################################
log() {
tailf -500 $RCS_LOG/console.log
}
###################################
#读取脚本的第一个参数($1)，进行判断
#参数取值范围：{start|debug|stop|restart|status|info|log}
#如参数不在指定范围之内，则打印帮助信息
###################################


case "$1" in
'start')
start
;;
'debug')
debug
;;
'stop')
stop
;;
'restart')
stop
start
;;
'status')
status
;;
'info')
info
;;
'log')
log
;;
'startjmx')
startjmx
;;
*)


echo "=======参数错误========="
echo "exp ：$0 start {start|debug|stop|restart|status|info|log}"
exit 1
esac
exit 0

