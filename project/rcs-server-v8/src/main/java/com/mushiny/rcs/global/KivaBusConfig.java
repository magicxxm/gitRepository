/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.global;

/**
 *协议公共类
 * @author aricochen
 */
public class KivaBusConfig {
    //消息出错吗
    public static byte ANALYSIS_DATAMESSAGE_ERROR=(byte)0xEE;
    //AGV到RCS报文头
    public static byte ROBOT2RCS_HEAD=(byte)0xAA;
    //RCS到AGV报文头
    public static byte RCS2ROBOT_HEAD=(byte)0xBB;
    //AGV到RCS报文最大字节数
    public static int AGV2RCS_MAX_BYTES = 1000;
    //功能字【终端类型验证，终端请求】
    public static byte TERMINAL_TYPE_VALIDATE_WORD_REQUEST=(byte)0X80;
    //功能字【终端类型验证，RCS应答】
    public static byte TERMINAL_TYPE_VALIDATE_WORD_RESPONSE=(byte)0X81;
    //终端类型ROBOT
    public static byte TERMINAL_TYPE_ROBOT=(byte)0x01;
    //终端类型CLIENT
    public static byte TERMINAL_TYPE_CLIENT=(byte)0x02;
    //终端类型APP
    public static byte TERMINAL_TYPE_APP=(byte)0x03;
}
