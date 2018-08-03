/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus;

import com.mushiny.rcs.global.CommandActionTypeConfig;

/**
 *清除已下发路径命令
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ClearPathCommand extends AbstractRobotCommand{
    private final byte commandCode = CommandActionTypeConfig.CLEAR_PATH_COMMAND;
    @Override
    public byte getCommandCode(){
        return commandCode;
    }
     public short getCommandParameter() {
        return commandParameter;
    }
    @Override
    public String toString(){
        return "commandCode=0x50,动作=清除已下发路径";
    }
}
