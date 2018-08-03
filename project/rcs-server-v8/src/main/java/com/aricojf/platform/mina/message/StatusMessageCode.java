/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

/**
 *状态消息码
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class StatusMessageCode {
    //未知
    public static byte UNKNOW = 0X00;
    //服务器已开启服务--服务器端用
    public static byte S_SERVER_OPEN_SERVICE = 0X01;
    //服务器已关闭服务--服务器端用
    public static byte S_SERVER_CLOSE_SERVICE = 0X02;
    //服务器正在开启服务--服务器端用
    public static byte S_SERVER_OPENING_SERVICE = 0X03;
    //客户端连接本服务器端成功--服务器端用
    public static byte S_CLIENT_CONNECTION_OPEN = 0X07;
    //客户端断开本服务器--服务器端用
    public static byte S_CLIENT_CONNECTION_CLOSE = 0X08;
    
    //客户端未连接服务器或连接失败
    public static byte CLIENT_CONNECTION_FAIL = 0X21;
    //客户端已连接服务器
    public static byte CLIENT_CONNECTION_SUCESS = 0X22;
    //客户端正在重新连接服务器
    public static byte CLIENT_CONNECTION_REPEAT = 0X23;
    //客户端正在连接服务器
    public static byte CLIENT_CONNECTION_ING = 0X24;
    
    
}
