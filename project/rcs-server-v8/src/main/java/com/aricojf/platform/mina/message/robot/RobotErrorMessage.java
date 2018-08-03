/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;

/**
 * 车辆故障数据包
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotErrorMessage extends RobotMessage {

    private int errorID;
    private short errorStatus;

    // mingchun.mu@mushiny.com -- 不匹配的数据
    private int preData;
    private int curData;
    // mingchun.mu@mushiny ---------------------
    
    public RobotErrorMessage() {
        this(-1);
    }

    public RobotErrorMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength=11;
        this.frameDataLength = 20;
        this.functionWordCode = Robot2RCSMessageTypeConfig.ROBOT_ERROR_MESSAGE;
         this.head = KivaBusConfig.ROBOT2RCS_HEAD;
    }
    public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];        
        //故障ID
        tmpMessageBody[0] = (byte) ((getErrorID()) & 0x00ff);
        tmpMessageBody[1] = (byte) ((getErrorID() >> 8) & 0x00ff);
        //故障状态
        tmpMessageBody[2] = (byte) ((getErrorStatus()) & 0x00ff);


        setMessageBody(tmpMessageBody);
        super.toMessage();
    }
   
    public void toObject() {
        super.toObject();
        byte[] messageBytes = (byte[])getMessage();
        //故障ID
        errorID = messageBytes[13];
        errorID = (int)((errorID << 8)|(messageBytes[12] & 0xff));
        //故障状态
        errorStatus = messageBytes[14];
        // 前一条数据
        preData = messageBytes[18];
        preData = (int)((preData << 8)|(messageBytes[17] & 0xff));
        preData = (int)((preData << 8)|(messageBytes[16] & 0xff));
        preData = (int)((preData << 8)|(messageBytes[15] & 0xff));
        // 当前数据
        curData = messageBytes[22];
        curData = (int)((curData << 8)|(messageBytes[21] & 0xff));
        curData = (int)((curData << 8)|(messageBytes[20] & 0xff));
        curData = (int)((curData << 8)|(messageBytes[19] & 0xff));
    }


    public int getErrorID() {
        return errorID;
    }
    public void setErrorID(int errorID) {
        this.errorID = errorID;
    }
    public short getErrorStatus() {
        return errorStatus;
    }
    public void setErrorStatus(short errorStatus) {
        this.errorStatus = errorStatus;
    }
    public int getPreData() {
        return preData;
    }
    public void setPreData(int preData) {
        this.preData = preData;
    }
    public int getCurData() {
        return curData;
    }
    public void setCurData(int curData) {
        this.curData = curData;
    }
    public String toString(){
          String tmp = "故障消息:\r\n"+super.toString();
          tmp+="\r\n";
          tmp+="故障ID:"+Integer.toHexString(getErrorID());
          tmp+="故障状态:"+Integer.toHexString(getErrorStatus());
          tmp+="前一条数据:"+Integer.toHexString(getErrorStatus());
          tmp+="当前数据:"+Integer.toHexString(getErrorStatus());
          return tmp;
      }

    
}
