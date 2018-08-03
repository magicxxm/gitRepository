/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus;

import com.mushiny.rcs.global.CommandActionTypeConfig;

/**
 *停到最近二维码
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class StopByNearCodeCommand extends AbstractRobotCommand {
    private final byte commandCode = CommandActionTypeConfig.STOP_NEAR_CODE_COMMAND;
    public byte getCommandCode(){
        return commandCode;
    }
     public short getCommandParameter() {
        return commandParameter;
    }
    public String toString(){
        return "commandCode=0xF0，动作=停到最近二维码,减速到0";
    }
   
}
