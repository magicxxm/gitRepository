/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

import com.aricojf.platform.mina.common.MachineInterface;
import java.net.InetSocketAddress;
import org.apache.mina.core.session.IoSession;

/**
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class SimpleMessage implements MessageInterface {

    protected byte msgTypeCode = (byte) 0x90;
    private MachineInterface machine;
    protected int msgDateTime;
    protected Object message;
    protected String messageTarget = null;
    private IoSession session;

    public SimpleMessage() {
        msgDateTime = (int) (System.currentTimeMillis()/1000);
    }

    public byte getMessageTypeCode() {
        return msgTypeCode;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public MachineInterface getMachine() {
        return machine;
    }

    public void setMachine(MachineInterface machine) {
        this.machine = machine;
    }

    /**
     * @return the msgDateTime
     */
    public int getMsgDateTime() {
        return msgDateTime;
    }

    /**
     * @param msgDateTime the msgDateTime to set
     */
    public void setMsgDateTime(int msgDateTime) {
        this.msgDateTime = msgDateTime;
    }
  
       public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public String toString() {
        return "msgTypeCode:" + getMessageTypeCode() + ",message:" + getMessage();
    }
}
