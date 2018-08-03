/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus;

import com.mushiny.rcs.global.CommandActionTypeConfig;

/**
 *旋转(托盘固定，且附带顶升)
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class Rotate12Command extends AbstractRotateCommand {
    private final byte commandCode = CommandActionTypeConfig.ROTATE_X12_COMMAND;
    public byte getCommandCode(){
        return commandCode;
    }
     public short getCommandParameter() {
        return commandParameter;
    }
    public String toString(){
        return "commandCode=0x12,动作=旋转(托盘固定，且附带顶升),旋转角度="+getCommandParameter();
    }
}
