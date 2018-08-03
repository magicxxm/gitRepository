package com.mushiny.rcs.kiva.bus;

import com.mushiny.rcs.global.CommandActionTypeConfig;

/**
 * 结束充电
 */
public class EndChargingCommand extends AbstractRobotCommand {
    private final byte commandCode = CommandActionTypeConfig.END_CHARGING_COMMAND;
    public byte getCommandCode(){
        return commandCode;
    }
     public short getCommandParameter() {
        return commandParameter;
    }
    public String toString(){
        return "commandCode=0x60,动作=结束充电";
    }
}
