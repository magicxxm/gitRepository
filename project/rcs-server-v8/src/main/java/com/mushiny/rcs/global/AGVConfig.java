/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.global;

/**
 * 旋转全局参数
 *
 * @author aricochen
 */
public class AGVConfig {

    public final static short AGV_STRAIGHT_SPEED_0 = 0;
    public final static short AGV_STRAIGHT_SPEED_1 = 1;
    public final static short AGV_STRAIGHT_SPEED_2 = 2;
    public final static short AGV_STRAIGHT_SPEED_3 = 3;
    public final static short AGV_STRAIGHT_SPEED_4 = 4;
    public final static short AGV_STRAIGHT_SPEED_5 = 5;
    public final static short AGV_ROTATE_SPEED_0 = 0;
    public final static short AGV_ROTATE_SPEED_1 = 0;
    public final static short AGV_ROTATE_SPEED_2 = 0;
    public final static short AGV_ROTATE_SPEED_3 = 0;
    public final static int ACTION_PARAM_ROTATE_0 = 0;
    public final static int ACTION_PARAM_ROTATE_90 = 90;
    public final static int ACTION_PARAM_ROTATE_180 = 180;
    public final static int ACTION_PARAM_ROTATE_270 = 270;
    public static int PRIVILEGE_ENTER_CELL = 516;
    public static int PRIVILEGE_ROTATION_CELL = 517;

    //锁格最大数量
    public static int AGV_SEND_PATH_MODEL_ALL = 0;
    public static int AGV_SEND_PATH_MODEL_LOCKED = 1;
    public static int AGV_SEND_PATH_MODEL = AGV_SEND_PATH_MODEL_LOCKED;
    public static int AGV_LOCKED_MAX_COUNTS = 8;
    public static int AGV_SEND_SP_COUNTS_ON_TIMEOUT=2;//AGV位置不改变时，尝试发送的次数
    //AGV状态
    public static int AGV_STATUS_NO_CHECK = -9;//未检查
    public static int AGV_STATUS_ERROR = -1;//错误状态
    public static int AGV_STATUS_SLEEPING = 0;//睡眠状态
    public static int AGV_STATUS_STANDBY = 1;//等待任务
    public static int AGV_STATUS_TASKED = 2;//已经赋予任务
    public static int AGV_STATUS_POWERING = 3;//充电状态
    public static int AGV_STATUS_STOP_NEAR_CODE = 4;//停到最近二维码状态
    public static int AGV_STATUS_TIMEOUT = 7;//心跳包或实时数据超时状态
    public static int AGV_STATUS_NO_POSITION_CHANGE = 8;//----AGV临时不保存此状态,只是向WCS预警!赋予任务,但位置不改变超时
    public static int AGV_STATUS_STOP_IMMEDIATELY = 10;//急停状态(需人工处理AGV)
    public static int AGV_STATUS_STOP_POWER = 13;//所有电机断电状态(需人工处理AGV)
    public static int AGV_STATUS_CLEAR_AGV_BUFFER_PATH = 16;//已清除AGV缓冲路径
    public static int AGV_STATUS_CLEAR_RCS_BUFFER_PATH = 19;//已清除RCS缓冲路径
    public static int  AGV_STATUS_NO_CONNECTION = 21;//TCP连接断开
    public static int AGV_STATUS_REPEAT_CONNECTION_ADD_ERROR = 24;//TCP断开重连后,位置错误(需人工处理AGV)
    public static long AGV_ACTIVE_INTERVAL_TIME = 3000; // agv激活时间间隔

    //通过状态码获得状态信息
    public static String getAGVStatusInfo(int status) {
        if (status == AGV_STATUS_NO_CHECK) {
            return "未检查";
        }
        if (status == AGV_STATUS_ERROR) {
            return "错误状态";
        }
        if (status == AGV_STATUS_SLEEPING) {
            return "睡眠状态";
        }
        if (status == AGV_STATUS_STANDBY) {
            return "等待任务";
        }
        if (status == AGV_STATUS_TASKED) {
            return "已经赋予任务";
        }
        if (status == AGV_STATUS_POWERING) {
            return "充电状态";
        }
        if (status == AGV_STATUS_STOP_NEAR_CODE) {
            return "停到最近二维码状态";
        }
        if (status == AGV_STATUS_STOP_IMMEDIATELY) {
            return "急停状态";
        }
        if (status == AGV_STATUS_STOP_POWER) {
            return "所有电机断电状态";
        }
        if (status == AGV_STATUS_CLEAR_AGV_BUFFER_PATH) {
            return "已清除AGV缓存路径";
        }
        if (status == AGV_STATUS_CLEAR_RCS_BUFFER_PATH) {
            return "已清除RCS缓存路径";
        }
        if (status == AGV_STATUS_NO_CONNECTION) {
            return "TCP连接断开";
        }
        if (status == AGV_STATUS_REPEAT_CONNECTION_ADD_ERROR) {
            return "TCP断开重连后,位置错误(需人工处理AGV)";
        }
        return "未知状态";
    }
    //AGV心跳执行时间
    public static int AGV_BEAT_LOOP_TIME = 500;//100毫秒
    //AGV状态检查间隔时间
    public static int AGV_CHECK_LOOP_TIME = 1;//2000毫秒
    //AGV心跳超时时间
    public static long AGV_BEAT_TIMEOUT = 20000;
    //AGV实时数据超时时间
    public static long AGV_RT_TIMEOUT = 20000;
    //AGV心跳或实时数据超时时间
    public static long AGV_BEAT_OR_RT_TIMEOUT = 10000;
    //AGV位置不改变超时时间
    public static long AGV_NO_POSITION_CHANGE_TIMEOUT = 20000;//配置文件读取
    //AGV锁格超时时间
    public static long AGV_LOCK_SP_TIMEOUT = 30000;//配置文件读取
     //============================命令码列表=====================================
    //启动
    public final static byte START_COMMAND = (byte)0x01;
    //停到最近二维码
    public final static byte STOP_NEAR_CODE_COMMAND = (byte)0xf0;
    //急停
    public final static byte STOP_IMMEDIATELY_COMMAND = (byte)0xf1;
    //所有电机供电断电
    public final static byte STOP_MOTO_POWER_COMMAND = (byte)0xf2;
    //旋转托盘固定
    public final static byte ROTATE_X10_COMMAND = (byte)0x10;
    //旋转(托盘跟着转动)
    public final static byte ROTATE_X11_COMMAND = (byte)0x11;
    //旋转(托盘固定,且附带顶升)
    public final static byte ROTATE_X12_COMMAND = (byte)0x12;
    //旋转(托盘固定,且附带降落)
    public final static byte ROTATE_X13_COMMAND = (byte)0x13;
    //顶升
    public final static byte POD_UP_COMMAND = (byte)0x20;
    //下降
    public final static byte POD_DOWN_COMMAND = (byte)0x21;
    //开始休眠
    public final static byte BEGIN_SLEEP_COMMAND = (byte)0x40;
    //结束休眠
    public final static byte STOP_SLEEP_COMMAND = (byte)0x41;
    //清除已下发路径
     public final static byte CLEAR_PATH_COMMAND = (byte)0x50;


     // mingchun.mu@mushiny.com  超时路径重发时间间隔
     public static int TIMEOUT_PATH_REGAIN_TIME = 3000;
     public static int FIRST_NODE_SEND_TIME_INTERVAL = 5000;
     public static int MIDEA_ERROR_FRAME_LENGTH = 0;
    // mingchun.mu@mushiny.com  -----------------------




}
