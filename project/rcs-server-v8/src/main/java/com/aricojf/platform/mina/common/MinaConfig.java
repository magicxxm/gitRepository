package com.aricojf.platform.mina.common;

import java.nio.charset.Charset;

/**
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MinaConfig {
    /*
    默认主机
     */
    public static String HOST_NAME="localhost";
    /*
   默认端口号
    */
    public static int PORT = 8894;
    /*
    连接超时时间，毫秒
     */
    public static int CONNECT_TIMEOUT = 150;
    /*
    读缓冲大小
     */
    public static int READ_BUFFER_SIZE = 1024 * 10;
    /*
    写缓冲大小
     */
    public static int WRITE_BUFFER_SIZE = 1024 * 2;
    /*
     多长时间进入空闲状态
    */
    public static int IDLE_TIME = 150;
    /*
     Client长连接时心跳包发送频率,3S
    */
    public static final int CLIENT_KEEP_ALIVE_TIME_INTERNAL = 3;
     /**
     * CLIENT长连接心跳包应答超时
     */
    public static final int CLIENT_KEEP_ALIVE_TIMEOUT_FOR_SERVER = 3;
    /**
     * Server长连接时心跳包检测频率，此时间需要大于CLIENT发送间隔时间，5 seconds,
     */
    public static final int SERVER_KEEP_ALIVE_TIME_INTERVAL = 5;
   

    /**
     * 心跳包 ping message
     */
    public static final String PING_MESSAGE="ping";
    /**
     * 心跳包 pong message
     */
    public static final String PONG_MESSAGE="pong";
    /*
    编码方式
     */
    public static Charset CHARSET_UTF8=Charset.forName("UTF-8");
    /*
    默认服务类型
     */
    public static int COMMON_SERVERTYPE=0;

    //---------------------------------------------------------------------
    /*
    Client尝试连接服务器间隔时间,秒
     */
    public static int CLIENT_CONNECT_SERVER_TIME_TERMINAL = 2;
}
