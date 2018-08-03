/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.common;

import org.apache.mina.core.session.IoSession;

/**
 *机器抽象接口
 * @author aricochen
 */
public interface MachineInterface {
    public long getID();
    public void setID(long ID);
    public String getIP();
    public void setIP(String ip);
    public int getPort();
    public void setPort(int port);
    public IoSession getSession();
    public void setSession(IoSession session);
    public boolean equals(MachineInterface mi);
    
}
