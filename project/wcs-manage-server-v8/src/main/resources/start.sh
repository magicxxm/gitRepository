#!/bin/bash
# $1 命令名称 $2 模块名称$3模块dockfile目录 $4 port $5 log配置
start() {

echo "开始 build 容器 $1 "
buildResult=$(docker build -t  $2 $3)

echo $buildResult | grep -n 'Successfully built'
if [ $? = 0 ]; then
echo "build 容器 $2 成功"
else
echo "build 容器 $2 失败"
fi
sleep 1

stopResult=$(docker stop  $2 || true)
echo $stopResult | grep -n $2
if [ $? = 0 ]; then
echo "停止容器$2成功"
else
echo "停止容器$2失败"
fi
sleep 1

rmResult=$(docker rm -f $2 || true)
echo $rmResult | grep -n $2
if [ $? = 0 ]; then
echo "删除容器$2成功"
else
echo "删除容器$2失败"
fi
sleep 1

if [ -n "$5" ];
 then
 echo docker run -idt -p $4 -e "TZ=Asia/Shanghai" --name $1 -v  $5 $2
else
echo docker run -idt -p $4 -e "TZ=Asia/Shanghai" --name $2  $2
fi

if [ $? = 0 ]; then
echo "启动容器$2成功"
else
echo "启动容器$2失败"
fi
}

check()
{
psResult=$(docker ps | grep $2)
echo $psResult | grep  $2
}
rcs_wcs_check()
{
 project=$(ps -ef | grep  $2)
 result=$(echo $project | awk '{print $2 " " $10}')
 pid=$(echo $project | awk '{print $2}')
 echo pid
}

 rcs_wcs_start()
 {
 project=$(ps -ef | grep  $1)
 pid=$(echo $project | awk '{print $2}')
 if [ ! -n "$pid" ] ;then
   echo $pid
 fi
 excute=$( nohup java -jar $2 & )
 echo $excute
 }
if [ "$1" = "start" ] ; then
 start $1 $2 $3 $4 $5
elif [ "$1" = "check" ] ; then
 check $2
elif [ "$1" = "rcs_wcs_start" ] ; then
 rcs_wcs_start $2
else
  echo "Usage: start( commands ... )"
fi


#!/bin/bash
# $1 命令名称 $2 模块名称$3模块dockfile目录 $4 port $5 log配置
start() {

echo "开始 build 容器 $1 "
if  [ "$1" != "wms-websocket-v8" ] ; then
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

if [ -n "$3" ];
 then
echo " 执行命令 docker run -idt -p $3 -e "TZ=Asia/Shanghai" --name $1 -v  $4 $1 "
 docker run -idt -p $3 -e "TZ=Asia/Shanghai" --name $1 -v  $4 $1
else
echo " 执行命令 docker run -idt -p $3 -e "TZ=Asia/Shanghai" --name $1  $1 "
docker run -idt -p $3 -e "TZ=Asia/Shanghai" --name $1  $1
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
pid=$(ps -aux | grep $1 | grep -v -e grep -e rcs_wcs_start -e rcs_wcs_stop | awk '{print $2}')
 if [ -n "$pid" ] ;then
 echo "check $1 success pid $pid"
else
pid=""
fi
}
stop()
{
stopResult=$(docker stop  $1 || true)
echo $stopResult | grep  $1
sleep 1
}
rcs_wcs_stop(){
 rcs_wcs_check $1
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
 rcs_wcs_start()
 {
rcs_wcs_stop $1
nohup java -jar $1 &
rcs_wcs_check $1
 }
if [ "$1" = "start" ] ; then
 start $2 $3 $4 $5
elif [ "$1" = "check" ] ; then
 check $2
elif [ "$1" = "rcs_wcs_start" ] ; then

 rcs_wcs_start $2
elif [ "$1" = "rcs_wcs_check" ] ; then
rcs_wcs_check $2
elif [ "$1" = "rcs_wcs_stop" ] ; then
rcs_wcs_stop $2
elif [ "$1" = "stop" ] ; then
stop $2
else
  echo "Usage: start( commands ... )"
fi

























