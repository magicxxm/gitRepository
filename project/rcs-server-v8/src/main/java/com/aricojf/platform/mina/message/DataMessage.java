/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

/**
 * 数据包
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class DataMessage extends SimpleMessage {
    protected byte msgTypeCode = (byte) 0x93;
    private byte [] messageBody;
     public DataMessage() {
        super();
    }
    /**
     * @return the messageBody
     */
    public byte[] getMessageBody() {
        return messageBody;
    }

    /**
     * @param messageBody the messageBody to set
     */
    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }
    /*
     获取消息体大小
    */
    public int getMessageBodyLength() {
        if(messageBody==null) {
            return 0;
        }
        return messageBody.length;
    }
    

}
