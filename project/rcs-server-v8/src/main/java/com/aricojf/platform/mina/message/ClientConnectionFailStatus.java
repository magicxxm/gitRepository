/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

/**
 *客户端连接服务器失败状态
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ClientConnectionFailStatus extends SimpleMessage{
    public byte statusCode = (byte)0x92;
    public String statusInfo = "连接服务器失败!";
}
