/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.common;

import java.net.InetSocketAddress;
import org.apache.mina.core.session.IoSession;

/**
 * 机器
 *
 * @author aricochen
 */
public class SimpleMachine implements MachineInterface {
    
    private String IP;
    private int port;
    private long ID;
    private IoSession session;
    
    public boolean equals(MachineInterface machine) {
        if(machine==null) {
            return false;
        }
        return getID()==machine.getID();
    }
    /**
     * @return the IP
     */
    public String getIP() {
        return IP;
    }

    /**
     * @param IP the IP to set
     */
    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
        InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
        if(address ==null ) {
            return;
        }
        setIP(address.getAddress().getHostAddress());
        setPort(address.getPort());
    }

    /**
     * @return the ID
     */
    public long getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(long ID) {
        this.ID = ID;
    }
    
}
