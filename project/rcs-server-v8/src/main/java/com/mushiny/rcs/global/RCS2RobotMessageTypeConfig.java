/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.global;

/**
 *
 * @author aricochen
 */
public class RCS2RobotMessageTypeConfig {
    //路径消息
    public static byte PATH_MESSAGE=(byte)0x01;
    //动作命令消息
    public static byte ACTION_COMMAND_MESSAGE=(byte)0x02;
    //心跳消息
    public static byte HEART_BEAT_RESPONSE_MESSAGE=(byte)0x03;
    //激活消息
    public static byte ACTIVE_ROBOT_MESSAGE=(byte)0x80;
    //配置消息
    public static byte CONFIG_ROBOT_MESSAGE=(byte)0x10;
    //ROBOT固件更新消息
    public static byte HARDWARE_UPDATE_MESSAGE=(byte)0x11;
    //ROBOT登录回复消息
    public static byte ROBOT_LOGIN_RRESPONSE_MESSAGE=(byte)0xf0;
    //回读ROBOT配置信息
    public static byte REQUEST_ROBOT_CONFIG_MESSAGE=(byte)0x12;

    // 美的故障命令字
    public static byte MEDIA_ERROR_MESSAGE = (byte) 0x04;
    // 美的故障命令字
    public static byte WALK_PARAMETER_CONFIG_MESSAGE = (byte) 0x05;

    
}
