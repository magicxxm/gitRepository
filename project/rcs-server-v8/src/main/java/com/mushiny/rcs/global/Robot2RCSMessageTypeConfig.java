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
public class Robot2RCSMessageTypeConfig {
    //ROBOT连接RCS SERVER消息
    public static byte ROBOT_CONNECTION_RCS_SERVER_OPEN=(byte)0x22;
    //ROBOT关闭RCS SERVER消息
    public static byte ROBOT_CONNECTION_RCS_SERVER_CLOSE=(byte)0x23;
    //ROBOT实时数据类型消息
    public static byte ROBOT_REALTIME_MESSAGE=(byte)0x01;
    //ROBOT心跳请求类型消息
    public static byte HEART_BEAT_REQUEST_MESSAGE=(byte)0x02;
    //ROBOT状态类型消息（周期性）
    public static byte ROBOT_STATUS_MESSAGE=(byte) 0x03;
    //ROBOT故障类型消息
    public static byte ROBOT_ERROR_MESSAGE=(byte) 0x04;
    //ROBOT登录类型消息
    public static byte ROBOT_LOGIN_REQUEST_MESSAGE=(byte) 0x10;
    //ROBOT配置消息
    public static byte ROBOT_RESPONSE_CONFIG_MESSAGE=(byte)0x05;
    //ROBOT动作命令回复消息
    public static byte ROBOT_REPONSE_ACTION_COMMAND_MESSAGE=(byte)0x06;

    // ROBOT动作 或动作命令完成 回复消息
    public static byte ROBOT_RESPONSE_ACTION_FINISHED_COMMAND_MESSAGE=(byte)0x09;

    // ROBOT 路径回复包 正常回复
    public static byte ROBOT_RESPONSE_NORMAL = (byte)0xa1;
    // ROBOT 路径回复包 异常回复
    public static byte ROBOT_RESPONSE_EXCEPTION = (byte)0xa0;
    // 美的故障包
    public static byte ROBOT_MEDIA_ERROR_MESSAGE = (byte)0xde;




}
