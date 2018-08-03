package com.mingchun.mu.aricojf.platform.mina.message.robot.response;

import com.aricojf.platform.mina.message.robot.RobotMessage;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;

/**
 *
 * 路径应答包 -- 正常
 *
 * Created by Laptop-6 on 2017/10/10.
 */
public class RCS2RobotActionResponseMessage extends RobotMessage {
    private byte commandWordBack;

    public RCS2RobotActionResponseMessage() {
        this(-1);
    }
    public RCS2RobotActionResponseMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength = 5;
        this.frameDataLength = 9 + messageBodyLength;
        this.functionWordCode = Robot2RCSMessageTypeConfig.ROBOT_RESPONSE_NORMAL;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;
    }

    @Override
    public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];
        tmpMessageBody[0] = getCommandWordBack();
        setMessageBody(tmpMessageBody);
        super.toMessage();
    }

    public byte getCommandWordBack() {
        return commandWordBack;
    }
    public void setCommandWordBack(byte commandWordBack) {
        this.commandWordBack = commandWordBack;
    }
}
