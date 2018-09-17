
# Provides:          php_fastcgi.sh
# Required-Start:    $local_fs $remote_fs $network $syslog
# Required-Stop:     $local_fs $remote_fs $network $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: starts the php_fastcgi daemon
# Description:       starts php_fastcgi using start-stop-daemon
### END INIT INFO
# $1 命令名称 $2 模块名称$3模块dockfile目录 $4 port $5 log配置 || [ "$1" = "registry-v8" ] || [ "$1" = "auth-server-v8" ] 
HOME_PATH=/home/mushiny/wms_v8
start() {
result=$(echo $1 | grep "pda" )
chmod 777 -R $2
sleep 1
echo "开始 build 容器 $1 "
if  [ "$1" != "websocket" ] && [ "$result" = "" ] ; then
buildResult=$(docker build -t  $1 $2)

echo $buildResult | grep -n 'Successfully built'
if [ $? = 0 ]; then
echo "build 容器 $1 成功"
else
echo "build 容器 $1 失败"
fi
sleep 1
fi

stopResult=$(docker stop  $1 || true)
echo $stopResult | grep -n $1
if [ $? = 0 ]; then
echo "停止容器$1成功"
else
echo "停止容器$1失败"
fi
sleep 1

rmResult=$(docker rm -f $1 || true)
echo $rmResult | grep -n $1
if [ $? = 0 ]; then
echo "删除容器$1成功"
else
echo "删除容器$1失败"
fi
sleep 1


if  [ "$1" != "websocket" ] && [ "$result" = "" ] ; then
 echo " 执行命令 docker run -idt -p $3 -e "TZ=Asia/Shanghai" --name $1 $1 "
 docker run -idt -p $3 -e "TZ=Asia/Shanghai" --name $1  $1
else
echo " 执行命令  docker run -idt -p $3 -e "TZ=Asia/Shanghai" -v /home/mushiny/wms_v8/$1/:/usr/local/tomcat/webapps/  -v /home/mushiny/logs:/home/log --name $1  tomcat "
 docker run -idt -p $3 -e "TZ=Asia/Shanghai" -v /home/mushiny/wms_v8/$1/:/usr/local/tomcat/webapps/  -v /home/mushiny/logs:/home/log --name $1  tomcat
fi

if [ $? = 0 ]; then
echo "启动容器$1成功"
else
echo "启动容器$1失败"
fi
}

check()
{
psResult=$(docker ps | grep $1)
echo $psResult | grep  $1
}
pid=""
rcs_wcs_check()
{
echo "开始check $1 " 
CLASSNAME=$HOME_PATH/$1/$1.jar
pid=`ps -ef |grep $(echo $CLASSNAME |awk -F/ '{print $NF}') | grep -v grep | awk '{print $2}'`	
echo "模块 $1 对应pid为$pid"
 if [ -n "$pid" ] ;then
 echo "check $1 success pid $pid"  
else
pid=""
fi
}

rcs_wcs_check_all()
{
for k in "${!module[@]}"
do 
pid=""
rcs_wcs_check ${module[$k]}

done

}
stop()
{
stopResult=$(docker stop  $1 || true)
echo $stopResult | grep  $1
sleep 1
}
rcs_wcs_stop(){
pid=""
rcs_wcs_check $1
if [ -n "$pid" ] ;then
kill -9 $pid
echo "杀死进程$pid"
else
  echo "程序没有启"
fi

}
rcs_wcs_stop2(){
pid=$(ps -aux | grep $1 | grep -v -e grep -e start -e stop | awk '{print $2}')
for pidTemp in $pid
do
 if [ -n "$pidTemp" ] ;then

 kill -9 $pidTemp
  echo "杀死进程$pidTemp"
else
  echo "程序没有启"
fi
done
}
module=(
wms-registryservice-v8
wms-gatewayservice-v8
wms-authservice-v8
wms-systemservice-v8  
wms-masterdata-service-v8       
wcs-server-v8
wms-midea-server
wms-midea-server-test
wms-client-v8	       
)
tomcat_start()
{
if  [ "$1" = "orderApp" ] ; then
sh /home/mushiny/wms_v8/tomcat_pda/bin/shutdown.sh
echo "关闭$1 tomcat"
sleep 3
sh /home/mushiny/wms_v8/tomcat_pda/bin/startup.sh
echo "启动$1 tomcat"
else
sh /home/mushiny/wms_v8/tomcat_websocket/bin/shutdown.sh
echo "关闭$1 tomcat"
sleep 3
sh /home/mushiny/wms_v8/tomcat_websocket/bin/startup.sh
echo "启动$1 tomcat"
fi
}
client_start()
{
if  [ "$1" = "wms-client-v8" ] ; then
start wms-client-v8 /home/mushiny/wms_v8/wms-client-v8 10001:80 
elif  [ "$1" = "wms-client-v8-pad" ] ; then
start wms-client-v8-pad /home/mushiny/wms_v8/wms-client-v8-pad 10002:80
else
echo "不支持的模块$1"
fi
}
 install()
{
sleep 10
for k in "${!module[@]}"
do 
echo "开始安装${module[$k]} " 
swith_start ${module[$k]}
done
}
 swith_start()
{
if [ "$1" = "websocket" ] ||[ "$1" = "orderApp" ] ; then
tomcat_start $1
elif [ "$1" = "wms-client-v8" ] ||[ "$1" = "wms-client-v8-pad" ] ; then
client_start $1
elif [ "$1" = "wcs-server-v8" ] ; then
rcs-wcs-start  
else
nohup_start $1
sleep 1
fi
}

rcs-wcs-start()
{
rcs-wcs-stop2
echo "启动wcs..."
nohup java  -jar $HOME_PATH/wcs-server-v8/wcs-server-v8.jar  > $HOME_PATH/wcs-server-v8/wcs-server-v8.out &
rcs_wcs_check wcs-server-v8
}
rcs-wcs-start2()
{
rcs-wcs-stop2
echo "启动wcs..."
nohup java  -jar $HOME_PATH/wcs-server-v8/wcs-server-v8.jar  > $HOME_PATH/wcs-server-v8/wcs-server-v8.out &
rcs_wcs_check wcs-server-v8
echo "启动rcs..."
sleep 30
nohup java -jar $HOME_PATH/rcs-server-v8/rcs-server-v8.jar  > $HOME_PATH/wcs-server-v8/rcs-server-v8.out &
rcs_wcs_check rcs-server-v8
}
rcs-wcs-stop2()
{
echo "开始停止rcs..."
rcs_wcs_stop rcs-server-v8
echo "开始停止wcs..."
rcs_wcs_stop wcs-server-v8
sleep 4

}
 nohup_start()
 {
rcs_wcs_stop $1
echo "删除out日志"
rm -rf $HOME_PATH/$1/$1.out

echo "执行 nohup java -Xms2g -Xmx2g -jar $HOME_PATH/$1/$1.jar  >$HOME_PATH/$1/$1.out & "
nohup java -Xms2g -Xmx2g -jar $HOME_PATH/$1/$1.jar  >$HOME_PATH/$1/$1.out &
rcs_wcs_check $1
sleep 1
 }

if [ "$1" = "start" ] ; then
 swith_start $2 
elif [ "$1" = "rcs_wcs" ] ; then
rcs-wcs-start
elif [ "$1" = "check" ] ; then
rcs_wcs_check $2
elif [ "$1" = "check_all" ] ; then
rcs_wcs_check_all 
elif [ "$1" = "rcs_wcs_stop" ] ; then
rcs-wcs-stop2 $2
elif [ "$1" = "stop" ] ; then
rcs_wcs_stop $2
elif [ "$1" = "install" ] ; then
install 
else
  echo "Usage: start( commands ... )"
fi






