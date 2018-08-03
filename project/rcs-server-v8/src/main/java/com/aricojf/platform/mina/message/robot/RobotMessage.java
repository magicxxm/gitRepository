/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.aricojf.platform.mina.message.DataMessage;
import com.aricojf.platform.security.CRC16;
import com.mushiny.rcs.global.KivaBusConfig;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 机器人数据消息
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotMessage extends DataMessage {
    protected long robotID;
    protected byte head;
    protected int messageBodyLength=-1;//消息体长度，子类应该设置此值
    protected int frameDataLength=-1;//帧长,子类应该设置此值
    protected byte functionWordCode;
    private int crc16;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RobotMessage() {
        super();
    }
    
    /*
     从二进制message中提取功能码
     数据流中第12个字节是功能码     
    */
    public byte getVelifyFunctionCode() {
        byte[] messageBytes = (byte[]) getMessage();  
        if(messageBytes==null) {
            return KivaBusConfig.ANALYSIS_DATAMESSAGE_ERROR;
        }
        if(messageBytes.length<12) {
            return KivaBusConfig.ANALYSIS_DATAMESSAGE_ERROR;
        }
        return messageBytes[11];
    }

    /**
     * @return the functionWordCode
     */
    public byte getFunctionWordCode() {
        return functionWordCode;
    }

    /**
     * @param functionWordCode the functionWordCode to set
     */
    public void setFunctionWordCode(byte functionWordCode) {
        this.functionWordCode = functionWordCode;
    }

    /**
     * @return the head
     */
    public byte getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(byte head) {
        this.head = head;
    }

    /**
     * @return the crc16
     */
    public int getCrc16() {
        return crc16;
    }

    /**
     * @param crc16 the crc16 to set
     */
    public void setCrc16(int crc16) {
        this.crc16 = crc16;
    }

    /**
     * @return the robotID
     */
    public long getRobotID() {
        return robotID;
    }

    /**
     * @param robotID the robotID to set
     */
    public void setRobotID(short robotID) {
        this.robotID = robotID;
    }

    /**
     * @return the frameDataLength
     */
    public int getFrameDataLength() {
        //车辆ID+时间戳+命令字+消息体长度
        return frameDataLength;
    }

    /**
     * @param frameDataLength the frameDataLength to set
     */
    public void setFrameDataLength(int frameDataLength) {
        this.frameDataLength = frameDataLength;
    }

    public void toMessage() {
        byte[] messageBytes = new byte[1 + 2 + getFrameDataLength() + 2];
        //消息头
        messageBytes[0] = head;
        messageBytes[1] = (byte) (getFrameDataLength() & 0x00ff);
        messageBytes[2] = (byte) ((getFrameDataLength() >> 8) & 0x00ff);
        messageBytes[3] = (byte) ((getRobotID()) & 0xff);
        messageBytes[4] = (byte) ((getRobotID() >> 8) & 0xff);
        messageBytes[5] = (byte) ((getRobotID() >> 16) & 0xff);
        messageBytes[6] = (byte) ((getRobotID() >> 24) & 0xff);

        messageBytes[7] = (byte) ((getMsgDateTime()) & 0xff);
        messageBytes[8] = (byte) ((getMsgDateTime() >> 8) & 0xff);
        messageBytes[9] = (byte) ((getMsgDateTime() >> 16) & 0xff);
        messageBytes[10] = (byte) ((getMsgDateTime() >> 24) & 0xff);
        messageBytes[11] = functionWordCode;
        //消息体
        byte[] messageBodyBytes = getMessageBody();
        for (int i = 0; i < getMessageBodyLength(); i++) {
            messageBytes[12 + i] = messageBodyBytes[i];
        }
        //CRC16计算，从帧长开始，直至CRC之前的字节总数，包含帧长在内
        int crcLen = 11 + getMessageBodyLength();//CRC计算时
        int crcRS = CRC16.calcCrc16(messageBytes, 1, crcLen);
        messageBytes[12 + getMessageBodyLength()] = (byte) (crcRS & 0xff);
        messageBytes[12 + getMessageBodyLength() + 1] = (byte) ((crcRS >> 8) & 0xff);
       
//        messageBytes[12 + getMessageBodyLength()] = (byte) ((crcRS >> 8) & 0xff);
//        messageBytes[12 + getMessageBodyLength()+1] = (byte) (crcRS & 0xff);
        setMessage(messageBytes);
    }

    public void toObject() {
        if (getMessage() == null) {
            return ;
        }
        if (getMessage() instanceof byte[]) {
            byte[] messageBytes = (byte[]) getMessage();            
            //帧头
            head = messageBytes[0];
            //帧长
            //messageHeadLength = messageBytes[2];
            //messageHeadLength = (int)((messageHeadLength << 8)|(messageBytes[1] & 0x000000ff));
            //车辆ID
            robotID = messageBytes[6];
            robotID = (robotID << 8) | (messageBytes[5] & 0xff);
            robotID = (robotID << 8) | (messageBytes[4] & 0xff);
            robotID = (robotID << 8) | (messageBytes[3] & 0xff);
            //时间戳
            int time=0;
            time = messageBytes[10] & 0xff;
            time = (time << 8) | (messageBytes[9] & 0xff);
            time = (time << 8) | (messageBytes[8] & 0xff);
            time = (time << 8) | (messageBytes[7] & 0xff);
            setMsgDateTime(time);
            //命令字
            functionWordCode = messageBytes[11];
        }
    }
    
      public String toString(){ 
          StringBuilder builder = new StringBuilder();
          builder.append("命令字:");
          builder.append(getFunctionWordCode());
          builder.append(",车辆:");
          builder.append(getRobotID());
          builder.append(",时间戳:");
          builder.append(dateFormat.format(new Date(getMsgDateTime()*1000L)));
        return builder.toString();
    }

}
