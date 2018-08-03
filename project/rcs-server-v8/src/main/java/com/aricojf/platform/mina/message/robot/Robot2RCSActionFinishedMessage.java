package com.aricojf.platform.mina.message.robot;

import com.aricojf.platform.common.Global;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;

/**
 *  robot -> rcs 动作及动作命令完成返回信息
 *
 * Created by Laptop-6 on 2017/9/7.
 */
public class Robot2RCSActionFinishedMessage extends RobotMessage{

    private int actedType;//完成动作类型 1byte
    private long addressCodeID;//完成动作时，小车所在地址码 4byte
    private int robotHeadToward;//车头朝向 2byte
    private long podCodeID;//货架码ID 4byte
    private int podAfaceToward;// 货架A面朝向
    private int robotCurStatus;// 小车当前状态
    private int robotCurStatusFlagBit; // 小车状态标识位


    public Robot2RCSActionFinishedMessage() {
        this(-1);
    }
    public Robot2RCSActionFinishedMessage(long robotID) {
        this.robotID = robotID;
        this.messageBodyLength=15;
        this.frameDataLength = 24;
        this.functionWordCode = Robot2RCSMessageTypeConfig.ROBOT_RESPONSE_ACTION_FINISHED_COMMAND_MESSAGE;
        this.head = KivaBusConfig.ROBOT2RCS_HEAD;
    }

    public void toObject() {
        byte[] messageBytes = (byte[]) getMessage();
        super.toObject();

        //完成动作类型
        actedType = messageBytes[12] & 0xff;

        //地址码ID
        addressCodeID = messageBytes[16];
        addressCodeID = (long) ((addressCodeID << 8) | (messageBytes[15] & 0xff));
        addressCodeID = (long) ((addressCodeID << 8) | (messageBytes[14] & 0xff));
        addressCodeID = (long) ((addressCodeID << 8) | (messageBytes[13] & 0xff));

        // 车头朝向
        robotHeadToward = messageBytes[18];
        robotHeadToward = ((robotHeadToward << 8) | (messageBytes[17] & 0xff));

        // 货架码 ID
        podCodeID = messageBytes[22];
        podCodeID = (long) ((podCodeID << 8) | (messageBytes[21] & 0xff));
        podCodeID = (long) ((podCodeID << 8) | (messageBytes[20] & 0xff));
        podCodeID = (long) ((podCodeID << 8) | (messageBytes[19] & 0xff));

        // 货架A面朝向
        podAfaceToward = messageBytes[24];
        podAfaceToward = ((podAfaceToward << 8) | (messageBytes[23] & 0xff));

        // 小车当前状态
        robotCurStatus = messageBytes[25];

        // 小车当前状态标识位
        robotCurStatusFlagBit = messageBytes[26];


    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("车辆动作完成消息:");
        builder.append(super.toString());
        builder.append(Global.NR);
        builder.append("，完成动作类型:");
        builder.append(actedType);
        builder.append("，地址码:");
        builder.append(addressCodeID);
        builder.append("，车头朝向:");
        builder.append(robotHeadToward);
        builder.append("，货架码：");
        builder.append(podCodeID);
        builder.append("，货架A面朝向：");
        builder.append(podAfaceToward);
        builder.append("，小车当前状态：");
        builder.append(robotCurStatus);
        builder.append("，小车状态标识位：");
        builder.append(robotCurStatusFlagBit);
        return builder.toString();
    }

    public int getActedType() {
        return actedType;
    }

    public void setActedType(int actedType) {
        this.actedType = actedType;
    }

    public long getAddressCodeID() {
        return addressCodeID;
    }

    public void setAddressCodeID(long addressCodeID) {
        this.addressCodeID = addressCodeID;
    }

    public int getRobotHeadToward() {
        return robotHeadToward;
    }

    public void setRobotHeadToward(int robotHeadToward) {
        this.robotHeadToward = robotHeadToward;
    }

    public long getPodCodeID() {
        return podCodeID;
    }

    public void setPodCodeID(long podCodeID) {
        this.podCodeID = podCodeID;
    }

    public int getPodAfaceToward() {
        return podAfaceToward;
    }

    public void setPodAfaceToward(int podAfaceToward) {
        this.podAfaceToward = podAfaceToward;
    }

    public int getRobotCurStatus() {
        return robotCurStatus;
    }

    public void setRobotCurStatus(int robotCurStatus) {
        this.robotCurStatus = robotCurStatus;
    }

    public int getRobotCurStatusFlagBit() {
        return robotCurStatusFlagBit;
    }

    public void setRobotCurStatusFlagBit(int robotCurStatusFlagBit) {
        this.robotCurStatusFlagBit = robotCurStatusFlagBit;
    }
}
