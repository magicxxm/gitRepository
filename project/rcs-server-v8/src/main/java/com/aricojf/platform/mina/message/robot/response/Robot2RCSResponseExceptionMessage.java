package com.aricojf.platform.mina.message.robot.response;

import com.aricojf.platform.mina.message.robot.RobotMessage;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;

/**
 *
 * 路径应答包 -- 异常
 *
 * Created by Laptop-6 on 2017/10/10.
 */
public class Robot2RCSResponseExceptionMessage extends RobotMessage {
    private byte commandWordBack;
    private long addressCodeID;
    public Robot2RCSResponseExceptionMessage() {
        this(-1);
    }
    public Robot2RCSResponseExceptionMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength = 5;
        this.frameDataLength = 9 + messageBodyLength;
        this.functionWordCode = Robot2RCSMessageTypeConfig.ROBOT_RESPONSE_EXCEPTION;
    }

    @Override
    public void toObject() {
        super.toObject();
        byte[] messageBytes = (byte[]) getMessage();
        commandWordBack = messageBytes[12];
        addressCodeID = messageBytes[16];
        addressCodeID = (addressCodeID << 8) | (messageBytes[15] & 0xff);
        addressCodeID = (addressCodeID << 8) | (messageBytes[14] & 0xff);
        addressCodeID = (addressCodeID << 8) | (messageBytes[13] & 0xff);
    }

    public byte getCommandWordBack() {
        return commandWordBack;
    }
    public void setCommandWordBack(byte commandWordBack) {
        this.commandWordBack = commandWordBack;
    }
    public long getAddressCodeID() {
        return addressCodeID;
    }
    public void setAddressCodeID(long addressCodeID) {
        this.addressCodeID = addressCodeID;
    }
}
