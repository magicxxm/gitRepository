/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

/**
 *服务器状态消息
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ServerStatusMessage extends SimpleMessage{
    protected byte msgTypeCode = (byte)0x93;
    private byte statusCode=StatusMessageCode.UNKNOW;
    public ServerStatusMessage() {
        super();
    }
      /**
     * @return the statusCode
     */
    public byte getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(byte statusCode) {
        this.statusCode = statusCode;
    }
    
}
