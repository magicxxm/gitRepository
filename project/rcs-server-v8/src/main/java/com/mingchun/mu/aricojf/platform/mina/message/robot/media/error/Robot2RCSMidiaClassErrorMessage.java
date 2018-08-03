package com.mingchun.mu.aricojf.platform.mina.message.robot.media.error;

import com.aricojf.platform.mina.message.robot.RobotMessage;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;

/**
 *
 * 美的分等级故障数据包
 *
 * Created by Laptop-6 on 2017/11/15.
 */
public class Robot2RCSMidiaClassErrorMessage extends RobotMessage {
    private byte[] expClassOne; // 异常等级一
    private byte[] expClassTwo; // 异常等级二
    private byte[] expClassThree; // 异常等级三
    private int expClassThreeLen; // 异常等级三字节数
    private byte errorStatus; // 故障状态
    public Robot2RCSMidiaClassErrorMessage() {
        this(-1);
    }
    public Robot2RCSMidiaClassErrorMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength = AGVConfig.MIDEA_ERROR_FRAME_LENGTH - 9;
        this.frameDataLength = AGVConfig.MIDEA_ERROR_FRAME_LENGTH;
        this.functionWordCode = RCS2RobotMessageTypeConfig.MEDIA_ERROR_MESSAGE;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;
    }
    private void init(){
        expClassOne = new byte[4];
        expClassTwo = new byte[4];
        expClassThreeLen = AGVConfig.MIDEA_ERROR_FRAME_LENGTH - 9 - 4 - 4 - 1;
        expClassThree = new byte[expClassThreeLen];
    }
    @Override
    public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];
        System.arraycopy(expClassOne, 0, tmpMessageBody, 12, 4);
        System.arraycopy(expClassTwo, 0, tmpMessageBody, 16, 4);
        System.arraycopy(expClassThree, 0, tmpMessageBody, 20, expClassThreeLen);
        setMessageBody(tmpMessageBody);
        super.toMessage();
    }
    @Override
    public void toObject() {
        byte[] messageBytes = (byte[]) getMessage();
        super.toObject();
        AGVConfig.MIDEA_ERROR_FRAME_LENGTH = getFrameDataLength();
        System.arraycopy(messageBytes, 12, expClassOne, 0, 4);
        System.arraycopy(messageBytes, 16, expClassTwo, 0, 4);
        System.arraycopy(messageBytes, 20, expClassThree, 0, expClassThreeLen);
        errorStatus = messageBytes[messageBytes.length-3];
    }


}
