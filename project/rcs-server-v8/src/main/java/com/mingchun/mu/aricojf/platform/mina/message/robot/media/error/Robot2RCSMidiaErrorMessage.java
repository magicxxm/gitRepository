package com.mingchun.mu.aricojf.platform.mina.message.robot.media.error;

import com.aricojf.platform.mina.message.robot.RobotMessage;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;

/**
 *
 * 美的故障数据包
 *
 * Created by Laptop-6 on 2017/11/15.
 */
public class Robot2RCSMidiaErrorMessage extends RobotMessage {
    private int generalErrorID; // 底盘一般性异常
    private int commonErrorID; // 底盘一般性异常
    private int seriousErrorID; // 底盘严重性异常
    private int logicErrorID; // 逻辑性异常
    private int errorStatus; // 故障状态
    public Robot2RCSMidiaErrorMessage() {
        this(-1);
    }
    public Robot2RCSMidiaErrorMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength = 11;
        this.frameDataLength = 9 + messageBodyLength;
        this.functionWordCode = RCS2RobotMessageTypeConfig.MEDIA_ERROR_MESSAGE;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;
    }
    @Override
    public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];
        tmpMessageBody[0] = (byte) ((getGeneralErrorID()) & 0xff);
        tmpMessageBody[1] = (byte) ((getGeneralErrorID() >> 8) & 0xff);
        tmpMessageBody[2] = (byte) ((getCommonErrorID()) & 0xff);
        tmpMessageBody[3] = (byte) ((getCommonErrorID() >> 8) & 0xff);
        tmpMessageBody[4] = (byte) ((getSeriousErrorID()) & 0xff);
        tmpMessageBody[5] = (byte) ((getSeriousErrorID() >> 8) & 0xff);
        tmpMessageBody[6] = (byte) ((getSeriousErrorID() >> 16) & 0xff);
        tmpMessageBody[7] = (byte) ((getSeriousErrorID() >> 24) & 0xff);
        tmpMessageBody[8] = (byte) ((getLogicErrorID()) & 0xff);
        tmpMessageBody[9] = (byte) ((getLogicErrorID() >> 8) & 0xff);
        tmpMessageBody[10] = (byte) ((getErrorStatus()) & 0xff);
        setMessageBody(tmpMessageBody);
        super.toMessage();
    }
    @Override
    public void toObject() {
        byte[] messageBytes = (byte[]) getMessage();
        super.toObject();
        generalErrorID = messageBytes[13];
        generalErrorID = (generalErrorID << 8) | (messageBytes[12] & 0xff);
        commonErrorID = messageBytes[15];
        commonErrorID = (commonErrorID << 8) | (messageBytes[14] & 0xff);
        seriousErrorID = messageBytes[19];
        seriousErrorID = (seriousErrorID << 8) | (messageBytes[18] & 0xff);
        seriousErrorID = (seriousErrorID << 8) | (messageBytes[17] & 0xff);
        seriousErrorID = (seriousErrorID << 8) | (messageBytes[16] & 0xff);
        logicErrorID = messageBytes[21];
        logicErrorID = (logicErrorID << 8) | (messageBytes[20] & 0xff);
        errorStatus = messageBytes[22];
    }

    public int getGeneralErrorID() {
        return generalErrorID;
    }

    public void setGeneralErrorID(int generalErrorID) {
        this.generalErrorID = generalErrorID;
    }

    public int getCommonErrorID() {
        return commonErrorID;
    }

    public void setCommonErrorID(int commonErrorID) {
        this.commonErrorID = commonErrorID;
    }

    public int getSeriousErrorID() {
        return seriousErrorID;
    }

    public void setSeriousErrorID(int seriousErrorID) {
        this.seriousErrorID = seriousErrorID;
    }

    public int getLogicErrorID() {
        return logicErrorID;
    }

    public void setLogicErrorID(int logicErrorID) {
        this.logicErrorID = logicErrorID;
    }

    public int getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(int errorStatus) {
        this.errorStatus = errorStatus;
    }
}
