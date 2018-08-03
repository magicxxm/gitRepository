/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.aricojf.platform.common.Global;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;
import com.sun.javafx.scene.control.skin.TextFieldSkin;

/**
 * 车辆配置数据包-------未完成
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCSWriteConfigMessage extends RobotMessage {
    //匹配字
    private short matchWord;
    //行进速度档位
    private short straightSpeed1;
    private short straightSpeed2;
    private short straightSpeed3;
    private short straightSpeed4;
    private short straightSpeed5;
    //转弯速度档位
    private short cornerSpeed1;
    private short cornerSpeed2;
    private short cornerSpeed3;
    //加减速度
    private short acceleration;
    private short dragAcceleration;
    //PID参数
    private float XP;
    private float XI;
    private float XD;
    private float thetaP;
    private float thetaI;
    private float thetaD;
    
    public RCSWriteConfigMessage() {
        this(-1);
    }

    public RCSWriteConfigMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength = 46;
        this.frameDataLength = 9 + messageBodyLength;
        this.functionWordCode = RCS2RobotMessageTypeConfig.CONFIG_ROBOT_MESSAGE;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;
    }

    public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];
        //匹配字临时保留
        tmpMessageBody[0] = 0x00;
        tmpMessageBody[1] = 0x00;
        //行进档位速度
        tmpMessageBody[2] = (byte) (getStraightSpeed1() & 0xff);
        tmpMessageBody[3] = (byte) ((getStraightSpeed1() >> 8) & 0xff);
        tmpMessageBody[4] = (byte) (getStraightSpeed2() & 0xff);
        tmpMessageBody[5] = (byte) ((getStraightSpeed2() >> 8) & 0xff);
        tmpMessageBody[6] = (byte) (getStraightSpeed3() & 0xff);
        tmpMessageBody[7] = (byte) ((getStraightSpeed3() >> 8) & 0xff);
        tmpMessageBody[8] = (byte) (getStraightSpeed4() & 0xff);
        tmpMessageBody[9] = (byte) ((getStraightSpeed4() >> 8) & 0xff);
        tmpMessageBody[10] = (byte) (getStraightSpeed5() & 0xff);
        tmpMessageBody[11] = (byte) ((getStraightSpeed5() >> 8) & 0xff);
        //转弯档位速度
        tmpMessageBody[12] = (byte) (getCornerSpeed1() & 0xff);
        tmpMessageBody[13] = (byte) ((getCornerSpeed1() >> 8) & 0xff);
        tmpMessageBody[14] = (byte) (getCornerSpeed2() & 0xff);
        tmpMessageBody[15] = (byte) ((getCornerSpeed2() >> 8) & 0xff);
        tmpMessageBody[16] = (byte) (getCornerSpeed3() & 0xff);
        tmpMessageBody[17] = (byte) ((getCornerSpeed3() >> 8) & 0xff);
        //加减速度
        tmpMessageBody[18] = (byte) (getAcceleration() & 0xff);
        tmpMessageBody[19] = (byte) ((getAcceleration() >> 8) & 0xff);
        tmpMessageBody[20] = (byte) (getDragAcceleration() & 0xff);
        tmpMessageBody[21] = (byte) ((getDragAcceleration() >> 8) & 0xff);
        //X PID
        int tmpPID = Float.floatToIntBits(getXP());
        tmpMessageBody[22] = (byte) (tmpPID & 0xff);
        tmpMessageBody[23] = (byte) ((tmpPID >> 8) & 0xff);
        tmpMessageBody[24] = (byte) ((tmpPID >> 16) & 0xff);
        tmpMessageBody[25] = (byte) ((tmpPID >> 24) & 0xff);
        tmpPID = Float.floatToIntBits(getXI());
        tmpMessageBody[26] = (byte) (tmpPID & 0xff);
        tmpMessageBody[27] = (byte) ((tmpPID >> 8) & 0xff);
        tmpMessageBody[28] = (byte) ((tmpPID >> 16) & 0xff);
        tmpMessageBody[29] = (byte) ((tmpPID >> 24) & 0xff);
        tmpPID = Float.floatToIntBits(getXD());
        tmpMessageBody[30] = (byte) (tmpPID & 0xff);
        tmpMessageBody[31] = (byte) ((tmpPID >> 8) & 0xff);
        tmpMessageBody[32] = (byte) ((tmpPID >> 16) & 0xff);
        tmpMessageBody[33] = (byte) ((tmpPID >> 24) & 0xff);
        //theta PID
        tmpPID = Float.floatToIntBits(getThetaP());
        tmpMessageBody[34] = (byte) (tmpPID & 0xff);
        tmpMessageBody[35] = (byte) ((tmpPID >> 8) & 0xff);
        tmpMessageBody[36] = (byte) ((tmpPID >> 16) & 0xff);
        tmpMessageBody[37] = (byte) ((tmpPID >> 24) & 0xff);
        tmpPID = Float.floatToIntBits(getThetaI());
        tmpMessageBody[38] = (byte) (tmpPID & 0xff);
        tmpMessageBody[39] = (byte) ((tmpPID >> 8) & 0xff);
        tmpMessageBody[40] = (byte) ((tmpPID >> 16) & 0xff);
        tmpMessageBody[41] = (byte) ((tmpPID >> 24) & 0xff);
        tmpPID = Float.floatToIntBits(getThetaD());
        tmpMessageBody[42] = (byte) (tmpPID & 0xff);
        tmpMessageBody[43] = (byte) ((tmpPID >> 8) & 0xff);
        tmpMessageBody[44] = (byte) ((tmpPID >> 16) & 0xff);
        tmpMessageBody[45] = (byte) ((tmpPID >> 24) & 0xff);
        setMessageBody(tmpMessageBody);
        super.toMessage();
    }

    public void toObject() {
        super.toObject();
        byte[] messageBytes = (byte[]) getMessage();
        //match word0
        matchWord = (short) (messageBytes[13] & 0xff);
        matchWord = (short) ((matchWord << 8) | (messageBytes[12] & 0xff));
        //straightSpeed1
        straightSpeed1 = (short) (messageBytes[15] & 0xff);
        straightSpeed1 = (short) ((straightSpeed1 << 8) | (messageBytes[14] & 0xff));
        //straightSpeed2
        straightSpeed2 = (short) (messageBytes[17] & 0xff);
        straightSpeed2 = (short) ((straightSpeed2 << 8) | (messageBytes[16] & 0xff));
        //straightSpeed3
        straightSpeed3 = (short) (messageBytes[19] & 0xff);
        straightSpeed3 = (short) ((straightSpeed3 << 8) | (messageBytes[18] & 0xff));
        //straightSpeed4
        straightSpeed4 = (short) (messageBytes[21] & 0xff);
        straightSpeed4 = (short) ((straightSpeed4 << 8) | (messageBytes[20] & 0xff));
        //straightSpeed5
        straightSpeed5 = (short) (messageBytes[23] & 0xff);
        straightSpeed5 = (short) ((straightSpeed5 << 8) | (messageBytes[22] & 0xff));
        //cornerSpeed1
        cornerSpeed1 = (short) (messageBytes[25] & 0xff);
        cornerSpeed1 = (short) ((cornerSpeed1 << 8) | (messageBytes[24] & 0xff));
        //cornerSpeed2
        cornerSpeed2 = (short) (messageBytes[27] & 0xff);
        cornerSpeed2 = (short) ((cornerSpeed2 << 8) | (messageBytes[26] & 0xff));
        //cornerSpeed3
        cornerSpeed3 = (short) (messageBytes[29] & 0xff);
        cornerSpeed3 = (short) ((cornerSpeed3 << 8) | (messageBytes[28] & 0xff));
        //acceleration
        acceleration = (short) (messageBytes[31] & 0xff);
        acceleration = (short) ((acceleration << 8) | (messageBytes[30] & 0xff));
        //dragAcceleration
        dragAcceleration = (short) (messageBytes[33] & 0xff);
        dragAcceleration = (short) ((dragAcceleration << 8) | (messageBytes[32] & 0xff));
        //PID
        int tmpPID = 0;
        float tmpFloatPID = 0.0f;
        //x.p
        tmpPID = (int) (messageBytes[37] & 0xff);
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[36] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[35] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[34] & 0xff));
        tmpFloatPID = Float.intBitsToFloat(tmpPID);
        setXP(tmpFloatPID);
         //x.i
         tmpPID = 0;
        tmpPID = (int) (messageBytes[41] & 0xff);
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[40] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[39] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[38] & 0xff));
        tmpFloatPID = Float.intBitsToFloat(tmpPID);
        setXI(tmpFloatPID);
         //x.d
         tmpPID = 0;
        tmpPID = (int) (messageBytes[44] & 0xff);
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[43] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[42] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[41] & 0xff));
        tmpFloatPID = Float.intBitsToFloat(tmpPID);
        setXD(tmpFloatPID);
          //theta.p
        tmpPID = 0;
        tmpPID = (int) (messageBytes[48] & 0xff);
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[47] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[46] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[45] & 0xff));
        tmpFloatPID = Float.intBitsToFloat(tmpPID);
        setThetaP(tmpFloatPID);
         //theta.i
        tmpPID = 0;
        tmpPID = (int) (messageBytes[52] & 0xff);
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[51] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[50] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[49] & 0xff));
        tmpFloatPID = Float.intBitsToFloat(tmpPID);
        setThetaI(tmpFloatPID);
         //theta.d
        tmpPID = 0;
        tmpPID = (int) (messageBytes[56] & 0xff);
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[55] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[54] & 0xff));
        tmpPID = (int) ((tmpPID << 8) | (messageBytes[53] & 0xff));
        tmpFloatPID = Float.intBitsToFloat(tmpPID);
        setThetaD(tmpFloatPID);
        
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("车辆配置消息:");
        builder.append(Global.NR);
        builder.append(super.toString());
        builder.append(Global.NR);
        builder.append("匹配字:");
        builder.append(getMatchWord());
        builder.append(Global.NR);
        builder.append("行进速度1:");
        builder.append(getStraightSpeed1());
        builder.append("行进速度2:");
        builder.append(getStraightSpeed2());
        builder.append("行进速度3:");
        builder.append(getStraightSpeed3());
        builder.append("行进速度4:");
        builder.append(getStraightSpeed4());
        builder.append("行进速度5:");
        builder.append(getStraightSpeed5());
        builder.append(Global.NR);
        builder.append("转弯速度1:");
        builder.append(getCornerSpeed1());
        builder.append("转弯速度2:");
        builder.append(getCornerSpeed2());
        builder.append("转弯速度3:");
        builder.append(getCornerSpeed3());
        builder.append("加速度:");
        builder.append(getAcceleration());
        builder.append("减速度:");
        builder.append(getDragAcceleration());
        builder.append(Global.NR);
        builder.append("xP:");
        builder.append(getXP());
        builder.append("xI:");
        builder.append(getXI());
        builder.append("xD:");
        builder.append(getXD());
        builder.append("thetaP:");
        builder.append(getThetaP());
        builder.append("thetaI:");
        builder.append(getThetaI());
        builder.append("thetaD:");
        builder.append(getThetaD());
        
        return builder.toString();
    }

    /**
     * @return the matchWord
     */
    public short getMatchWord() {
        return matchWord;
    }

    /**
     * @param matchWord the matchWord to set
     */
    public void setMatchWord(short matchWord) {
        this.matchWord = matchWord;
    }

    /**
     * @return the straightSpeed1
     */
    public short getStraightSpeed1() {
        return straightSpeed1;
    }

    /**
     * @param straightSpeed1 the straightSpeed1 to set
     */
    public void setStraightSpeed1(short straightSpeed1) {
        this.straightSpeed1 = straightSpeed1;
    }

    /**
     * @return the straightSpeed2
     */
    public short getStraightSpeed2() {
        return straightSpeed2;
    }

    /**
     * @param straightSpeed2 the straightSpeed2 to set
     */
    public void setStraightSpeed2(short straightSpeed2) {
        this.straightSpeed2 = straightSpeed2;
    }

    /**
     * @return the straightSpeed3
     */
    public short getStraightSpeed3() {
        return straightSpeed3;
    }

    /**
     * @param straightSpeed3 the straightSpeed3 to set
     */
    public void setStraightSpeed3(short straightSpeed3) {
        this.straightSpeed3 = straightSpeed3;
    }

    /**
     * @return the straightSpeed4
     */
    public short getStraightSpeed4() {
        return straightSpeed4;
    }

    /**
     * @param straightSpeed4 the straightSpeed4 to set
     */
    public void setStraightSpeed4(short straightSpeed4) {
        this.straightSpeed4 = straightSpeed4;
    }

    /**
     * @return the straightSpeed5
     */
    public short getStraightSpeed5() {
        return straightSpeed5;
    }

    /**
     * @param straightSpeed5 the straightSpeed5 to set
     */
    public void setStraightSpeed5(short straightSpeed5) {
        this.straightSpeed5 = straightSpeed5;
    }

    /**
     * @return the cornerSpeed1
     */
    public short getCornerSpeed1() {
        return cornerSpeed1;
    }

    /**
     * @param cornerSpeed1 the cornerSpeed1 to set
     */
    public void setCornerSpeed1(short cornerSpeed1) {
        this.cornerSpeed1 = cornerSpeed1;
    }

    /**
     * @return the cornerSpeed2
     */
    public short getCornerSpeed2() {
        return cornerSpeed2;
    }

    /**
     * @param cornerSpeed2 the cornerSpeed2 to set
     */
    public void setCornerSpeed2(short cornerSpeed2) {
        this.cornerSpeed2 = cornerSpeed2;
    }

    /**
     * @return the cornerSpeed3
     */
    public short getCornerSpeed3() {
        return cornerSpeed3;
    }

    /**
     * @param cornerSpeed3 the cornerSpeed3 to set
     */
    public void setCornerSpeed3(short cornerSpeed3) {
        this.cornerSpeed3 = cornerSpeed3;
    }

    /**
     * @return the acceleration
     */
    public short getAcceleration() {
        return acceleration;
    }

    /**
     * @param acceleration the acceleration to set
     */
    public void setAcceleration(short acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * @return the dragAcceleration
     */
    public short getDragAcceleration() {
        return dragAcceleration;
    }

    /**
     * @param dragAcceleration the dragAcceleration to set
     */
    public void setDragAcceleration(short dragAcceleration) {
        this.dragAcceleration = dragAcceleration;
    }

    /**
     * @return the XP
     */
    public float getXP() {
        return XP;
    }

    /**
     * @param XP the XP to set
     */
    public void setXP(float XP) {
        this.XP = XP;
    }

    /**
     * @return the XI
     */
    public float getXI() {
        return XI;
    }

    /**
     * @param XI the XI to set
     */
    public void setXI(float XI) {
        this.XI = XI;
    }

    /**
     * @return the XD
     */
    public float getXD() {
        return XD;
    }

    /**
     * @param XD the XD to set
     */
    public void setXD(float XD) {
        this.XD = XD;
    }

    /**
     * @return the thetaP
     */
    public float getThetaP() {
        return thetaP;
    }

    /**
     * @param thetaP the thetaP to set
     */
    public void setThetaP(float thetaP) {
        this.thetaP = thetaP;
    }

    /**
     * @return the thetaI
     */
    public float getThetaI() {
        return thetaI;
    }

    /**
     * @param thetaI the thetaI to set
     */
    public void setThetaI(float thetaI) {
        this.thetaI = thetaI;
    }

    /**
     * @return the thetaD
     */
    public float getThetaD() {
        return thetaD;
    }

    /**
     * @param thetaD the thetaD to set
     */
    public void setThetaD(float thetaD) {
        this.thetaD = thetaD;
    }
    
}
