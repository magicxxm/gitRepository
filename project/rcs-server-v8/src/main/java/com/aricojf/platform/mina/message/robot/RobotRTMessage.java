/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.aricojf.platform.common.Global;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;

/**
 * 车辆实时数据
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotRTMessage extends RobotMessage {

    private long addressCodeID;//地址码ID
    private short addressCodeInfoX;//地址码信息，X坐标，2字节有符号整数
    private short addressCodeInfoY;//地址码信息，Y坐标，2字节有符号整数
    private float addressCodeInfoTheta;//地址码信息，θ角度，为4字节单精度浮点型
    private long podCodeID;//货架码ID
    private short podCodeInfoX;//货架码信息，X坐标，2字节有符号整数
    private short podCodeInfoY;//货架码信息，Y坐标，2字节有符号整数
    private float podCodeInfoTheta;//货架码信息，θ角度，为4字节单精度浮点
    private short speed;//速度档位
    
    public RobotRTMessage() {
       this(-1);
    }
    public RobotRTMessage(long robotID) {
        this.robotID = robotID;
        this.messageBodyLength=25;
        this.frameDataLength = 34;
        this.functionWordCode = Robot2RCSMessageTypeConfig.ROBOT_REALTIME_MESSAGE;
         this.head = KivaBusConfig.ROBOT2RCS_HEAD;
    }


    public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];
        //地址码ID
        tmpMessageBody[0] = (byte) ((getAddressCodeID()) & 0xff);
        tmpMessageBody[1] = (byte) ((getAddressCodeID() >> 8) & 0xff);
        tmpMessageBody[2] = (byte) ((getAddressCodeID() >> 16) & 0xff);
        tmpMessageBody[3] = (byte) ((getAddressCodeID() >> 24) & 0xff);
        //地址码信息，X
        tmpMessageBody[4] = (byte) ((getAddressCodeInfoX()) & 0xff);
        tmpMessageBody[5] = (byte) ((getAddressCodeInfoX() >> 8) & 0xff);
        //地址码信息，y
        tmpMessageBody[6] = (byte) ((getAddressCodeInfoY()) & 0xff);
        tmpMessageBody[7] = (byte) ((getAddressCodeInfoY() >> 8) & 0xff);
        //地址码信息，θ角度
        tmpMessageBody[8] = (byte) ((Float.floatToIntBits(getAddressCodeInfoTheta())) & 0xff);
        tmpMessageBody[9] = (byte) ((Float.floatToIntBits(getAddressCodeInfoTheta()) >> 8) & 0xff);
        tmpMessageBody[10] = (byte) ((Float.floatToIntBits(getAddressCodeInfoTheta()) >> 16) & 0xff);
        tmpMessageBody[11] = (byte) ((Float.floatToIntBits(getAddressCodeInfoTheta()) >> 24) & 0xff);
        //货架码ID
        tmpMessageBody[12] = (byte) ((getPodCodeID()) & 0xff);
        tmpMessageBody[13] = (byte) ((getPodCodeID() >> 8) & 0xff);
        tmpMessageBody[14] = (byte) ((getPodCodeID() >> 16) & 0xff);
        tmpMessageBody[15] = (byte) ((getPodCodeID() >> 24) & 0xff);
        //货架码信息，X
        tmpMessageBody[16] = (byte) ((getPodCodeInfoX()) & 0xff);
        tmpMessageBody[17] = (byte) ((getPodCodeInfoX() >> 8) & 0xff);
        //货架码信息，y
        tmpMessageBody[18] = (byte) ((getPodCodeInfoY()) & 0xff);
        tmpMessageBody[19] = (byte) ((getPodCodeInfoY() >> 8) & 0xff);
        //货架码信息，θ角度
        tmpMessageBody[20] = (byte) ((Float.floatToIntBits(getPodCodeInfoTheta())) & 0xff);
        tmpMessageBody[21] = (byte) ((Float.floatToIntBits(getPodCodeInfoTheta()) >> 8) & 0xff);
        tmpMessageBody[22] = (byte) ((Float.floatToIntBits(getPodCodeInfoTheta()) >> 16) & 0xff);
        tmpMessageBody[23] = (byte) ((Float.floatToIntBits(getPodCodeInfoTheta()) >> 24) & 0xff);
        //速度档位
        tmpMessageBody[24] = (byte)getSpeed();
        setMessageBody(tmpMessageBody);
        super.toMessage();
    }

    public void toObject() {
        byte[] messageBytes = (byte[]) getMessage();
        super.toObject();
        //地址码ID
        addressCodeID = messageBytes[15];
        addressCodeID = (long) ((addressCodeID << 8) | (messageBytes[14] & 0xff));
        addressCodeID = (long) ((addressCodeID << 8) | (messageBytes[13] & 0xff));
        addressCodeID = (long) ((addressCodeID << 8) | (messageBytes[12] & 0xff));
        //地址码信息X【有符号，16整形】
        addressCodeInfoX = messageBytes[17];
        addressCodeInfoX = (short) ((addressCodeInfoX << 8) | (messageBytes[16] & 0xff));
        //地址码信息Y【有符号，16整形】
        addressCodeInfoY = messageBytes[19];
        addressCodeInfoY = (short) ((addressCodeInfoY << 8) | (messageBytes[18] & 0xff));
        //地址码θ【有符号，32位单精度浮点型】
        int theta = 0;
        theta = messageBytes[23];
        theta = (int) ((theta << 8) | (messageBytes[22] & 0xff));
        theta = (int) ((theta << 8) | (messageBytes[21] & 0xff));
        theta = (int) ((theta << 8) | (messageBytes[20] & 0xff));
        addressCodeInfoTheta = Float.intBitsToFloat(theta);

        //货架码ID
        podCodeID = messageBytes[27];
        podCodeID = (long) ((podCodeID << 8) | (messageBytes[26] & 0xff));
        podCodeID = (long) ((podCodeID << 8) | (messageBytes[25] & 0xff));
        podCodeID = (long) ((podCodeID << 8) | (messageBytes[24] & 0xff));
        //货架码信息X【有符号，16整形】
        podCodeInfoX = messageBytes[29];
        podCodeInfoX = (short) ((podCodeInfoX << 8) | (messageBytes[28] & 0x00ff));
        //货架码信息Y【有符号，16整形】
        podCodeInfoY = messageBytes[31];
        podCodeInfoY = (short) ((podCodeInfoY << 8) | (messageBytes[30] & 0x00ff));
        //货架码信息θ【有符号，32位单精度浮点型】
        theta = 0;
        theta = messageBytes[35];
        theta = (int) ((theta << 8) | (messageBytes[34] & 0xff));
        theta = (int) ((theta << 8) | (messageBytes[33] & 0xff));
        theta = (int) ((theta << 8) | (messageBytes[32] & 0xff));
        podCodeInfoTheta = Float.intBitsToFloat(theta);
        //速度档位
        speed = messageBytes[36];
    }
     public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("车辆实时消息:");
        builder.append(super.toString());
        builder.append(Global.NR);
        builder.append("地址码ID:");
        builder.append(getAddressCodeID());
        builder.append("地址码信息X:");
        builder.append(getAddressCodeInfoX());
        builder.append("地址码信息Y:");
        builder.append(getAddressCodeInfoY());
        builder.append("地址码信息theta");
        builder.append(getAddressCodeInfoTheta());
        builder.append(Global.NR);
        builder.append("货架码ID:");
        builder.append(getPodCodeID());
        builder.append("货架码信息X:");
        builder.append(getPodCodeInfoX());
        builder.append("货架码信息Y:");
        builder.append(getPodCodeInfoX());
        builder.append("货架码信息theta:");
        builder.append(getPodCodeInfoTheta());
        builder.append("速度档位:");
        builder.append(getSpeed());
        return builder.toString();
    }

    /**
     * @return the addressCodeID
     */
    public long getAddressCodeID() {
        return addressCodeID;
    }

    /**
     * @param addressCodeID the addressCodeID to set
     */
    public void setAddressCodeID(long addressCodeID) {
        this.addressCodeID = addressCodeID;
    }

    /**
     * @return the addressCodeInfoX
     */
    public short getAddressCodeInfoX() {
        return addressCodeInfoX;
    }

    /**
     * @param addressCodeInfoX the addressCodeInfoX to set
     */
    public void setAddressCodeInfoX(short addressCodeInfoX) {
        this.addressCodeInfoX = addressCodeInfoX;
    }

    /**
     * @return the addressCodeInfoY
     */
    public short getAddressCodeInfoY() {
        return addressCodeInfoY;
    }

    /**
     * @param addressCodeInfoY the addressCodeInfoY to set
     */
    public void setAddressCodeInfoY(short addressCodeInfoY) {
        this.addressCodeInfoY = addressCodeInfoY;
    }

    /**
     * @return the addressCodeInfoTheta
     */
    public float getAddressCodeInfoTheta() {
        return addressCodeInfoTheta;
    }

    /**
     * @param addressCodeInfoTheta the addressCodeInfoTheta to set
     */
    public void setAddressCodeInfoTheta(float addressCodeInfoTheta) {
        this.addressCodeInfoTheta = addressCodeInfoTheta;
    }

    /**
     * @return the podCodeID
     */
    public long getPodCodeID() {
        return podCodeID;
    }

    /**
     * @param podCodeID the podCodeID to set
     */
    public void setPodCodeID(long podCodeID) {
        this.podCodeID = podCodeID;
    }

    /**
     * @return the podCodeInfoX
     */
    public short getPodCodeInfoX() {
        return podCodeInfoX;
    }

    /**
     * @param podCodeInfoX the podCodeInfoX to set
     */
    public void setPodCodeInfoX(short podCodeInfoX) {
        this.podCodeInfoX = podCodeInfoX;
    }

    /**
     * @return the podCodeInfoY
     */
    public short getPodCodeInfoY() {
        return podCodeInfoY;
    }

    /**
     * @param podCodeInfoY the podCodeInfoY to set
     */
    public void setPodCodeInfoY(short podCodeInfoY) {
        this.podCodeInfoY = podCodeInfoY;
    }

    /**
     * @return the podCodeInfoTheta
     */
    public float getPodCodeInfoTheta() {
        return podCodeInfoTheta;
    }

    /**
     * @param podCodeInfoTheta the podCodeInfoTheta to set
     */
    public void setPodCodeInfoTheta(float podCodeInfoTheta) {
        this.podCodeInfoTheta = podCodeInfoTheta;
    }

    /**
     * @return the speed
     */
    public short getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(short speed) {
        this.speed = speed;
    }
    
}
