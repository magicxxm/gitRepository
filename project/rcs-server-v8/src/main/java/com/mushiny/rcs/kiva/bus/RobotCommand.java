/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus;

/**
 *命令码接口
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface RobotCommand {
    public byte getCommandCode();
    public short getCommandParameter();
    public void setCommandParameter(short parameter);
    public String toString();
}
