/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

/**
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ClientConnectLogMessage extends SimpleMessage{
    protected byte msgTypeCode = (byte)0x92;
    private boolean connectionSucess=false;
    /**
     * @return the connectionSucess
     */
    public boolean isConnectionSucess() {
        return connectionSucess;
    }

    /**
     * @param connectionSucess the connectionSucess to set
     */
    public void setConnectionSucess(boolean connectionSucess) {
        this.connectionSucess = connectionSucess;
    }
    
    
}
