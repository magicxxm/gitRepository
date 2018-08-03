/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.aricojf.platform.common.HexBinaryUtil;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;

/**
 *心跳数据包-ROBOT发送
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotHeartBeatRequestMessage extends RobotMessage{
    private long addressCodeID;
    public RobotHeartBeatRequestMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength=4;
        this.frameDataLength=9+messageBodyLength;//心跳包帧长
        this.functionWordCode=Robot2RCSMessageTypeConfig.HEART_BEAT_REQUEST_MESSAGE;
        this.head = KivaBusConfig.ROBOT2RCS_HEAD;
    }
     public RobotHeartBeatRequestMessage() {
        this(-1);
    }
      public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];
        //地址码ID
        tmpMessageBody[0] = (byte) ((getAddressCodeID()) & 0xff);
        tmpMessageBody[1] = (byte) ((getAddressCodeID() >> 8) & 0xff);
        tmpMessageBody[2] = (byte) ((getAddressCodeID() >> 16) & 0xff);
        tmpMessageBody[3] = (byte) ((getAddressCodeID() >> 24) & 0xff);
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
      }
     public String toString(){
        return "心跳数据包:\r\n"+super.toString()+",地址码="+addressCodeID;
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
    
   
}
