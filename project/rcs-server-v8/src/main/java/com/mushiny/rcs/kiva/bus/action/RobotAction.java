/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

/**
 *ROBOT动作接口
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface RobotAction {
    public byte getActionCode();
    public int getActionParameter();
    public void setActionParameter(int actionParameter);
    public short getSpeed();
    public void setSpeed(short speed);
    public byte[] toBytes();
    public String toString();
}
