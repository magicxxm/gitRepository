/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.global;

/**
 *动作码\命令码
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class CommandActionTypeConfig {
    
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
    // 开始充电
    public final static byte START_CHARGING_COMMAND = (byte) 0x30;
    // 结束充电
    public final static byte END_CHARGING_COMMAND = (byte) 0x60;


}
