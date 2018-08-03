/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.CommandActionTypeConfig;
import com.mushiny.rcs.kiva.bus.*;

/**
 *命令消息
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public abstract class AbstractActionCommandMessage extends RobotMessage {
    
    protected RobotCommand command;
    protected byte commandCode;
    protected short commandParameter;

    public AbstractActionCommandMessage() {
        super();
    }

    public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];
        //动作命令码
        tmpMessageBody[0] = (byte) ((getCommandCode()) & 0xff);
        //--tmpMessageBody[0] = agvAction.getActionCode();
        //动作命令参数
        tmpMessageBody[1] = (byte) ((getCommandParameter()) & 0xff);
        tmpMessageBody[2] = (byte) ((getCommandParameter() >> 8) & 0xff);
        //--tmpMessageBody[1] = (byte)(agvAction.getActionParameter() & 0xff);
        //--tmpMessageBody[2] =  (byte)((agvAction.getActionParameter() >> 8) & 0xff);
        setMessageBody(tmpMessageBody);
        super.toMessage();
    }

    public void toObject() {
        byte[] messageBytes = (byte[]) getMessage();
        super.toObject();
        //动作命令码
        commandCode = messageBytes[12];
        //动作命令参数
        commandParameter = messageBytes[14];
        commandParameter = (short) ((commandParameter << 8) | (messageBytes[13] & 0xff));
        //
        if (commandCode == CommandActionTypeConfig.START_COMMAND) {
            command = new StartCommand();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.STOP_NEAR_CODE_COMMAND) {
            command = new StopByNearCodeCommand();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.STOP_IMMEDIATELY_COMMAND) {
            command = new StopImmediatelyCommand();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.STOP_MOTO_POWER_COMMAND) {
            command = new StopImmediatelyCommand();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.ROTATE_X10_COMMAND) {
            command = new Rotate10Command();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.ROTATE_X11_COMMAND) {
            command = new Rotate11Command();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.ROTATE_X12_COMMAND) {
            command = new Rotate12Command();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.ROTATE_X13_COMMAND) {
            command = new Rotate13Command();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.POD_UP_COMMAND) {
            command = new UpCommand();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.POD_DOWN_COMMAND) {
            command = new DownCommand();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.BEGIN_SLEEP_COMMAND) {
            command = new BeginSleepCommand();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.STOP_SLEEP_COMMAND) {
            command = new StopSleepCommand();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
        if (commandCode == CommandActionTypeConfig.CLEAR_PATH_COMMAND) {
            command = new ClearPathCommand();
            getCommand().setCommandParameter(commandParameter);
            return;
        }
    }

    /**
     * @return the commandCode
     */
    public short getCommandCode() {
        return commandCode;
    }

    /**
     * @param commandCode the commandCode to set
     */
    public void setCommandCode(byte commandCode) {
        this.commandCode = commandCode;
    }

    /**
     * @return the commandParameter
     */
    public short getCommandParameter() {
        return commandParameter;
    }

    /**
     * @param commandParameter the commandParameter to set
     */
    public void setCommandParameter(short commandParameter) {
        this.commandParameter = commandParameter;
    }

    /**
     * @return the command
     */
    public RobotCommand getCommand() {
        return command;
    }
    
}
