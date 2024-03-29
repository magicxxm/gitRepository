/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;
import com.mushiny.rcs.kiva.bus.RobotCommand;

/**
 * 车辆动作命令消息
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCS2RobotActionCommandMessage extends AbstractActionCommandMessage {

    public RCS2RobotActionCommandMessage(long robotID) {
        this(robotID, null);
    }

    public RCS2RobotActionCommandMessage(long robotID, RobotCommand command) {
        super();
        this.robotID = robotID;
        this.messageBodyLength = 3;
        this.frameDataLength = 12;
        this.functionWordCode = RCS2RobotMessageTypeConfig.ACTION_COMMAND_MESSAGE;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;
        this.command = command;
        if (command != null) {
            commandCode = command.getCommandCode();
            commandParameter = command.getCommandParameter();
        }
    }

    public String toString() {
        String tmp = super.toString();
        tmp += "\r\n";
        tmp += "命令码:" + Integer.toHexString(getCommandCode());
        tmp += "命令参数:" + Integer.toHexString(getCommandParameter());
        return tmp;
    }
}
