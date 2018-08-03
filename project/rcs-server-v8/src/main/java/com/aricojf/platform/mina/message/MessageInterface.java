/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

import com.aricojf.platform.mina.common.MachineInterface;
import org.apache.mina.core.session.IoSession;

/**
 *客户端状态接口
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface MessageInterface {
    public byte getMessageTypeCode();
    public Object getMessage();
    public void setMessage(Object msg);
    public MachineInterface getMachine();
    public void setMachine(MachineInterface machine);
     public IoSession getSession();
    public void setSession(IoSession session);
   
    public String toString();
}
