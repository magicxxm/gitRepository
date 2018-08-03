/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;

/**
 *回读配置信息
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCSRequestConfigMessage extends RobotMessage{
    private short matchWord=0;
     public RCSRequestConfigMessage() {
        this(-1);
    }

    public RCSRequestConfigMessage(long robotID) {
        super();
       this.robotID = robotID;
        this.messageBodyLength = 2;
        this.frameDataLength = 11;
        this.functionWordCode = RCS2RobotMessageTypeConfig.REQUEST_ROBOT_CONFIG_MESSAGE;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;
    }
     public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];
        tmpMessageBody[0] = (byte) ((getMatchWord()) & 0xff);
        tmpMessageBody[1] = (byte) ((getMatchWord() >> 8) & 0xff);
         setMessageBody(tmpMessageBody);
        super.toMessage();
     }
     
    public String toString(){
        return "配置数据包回读request:\r\n"+super.toString();
    }

    /**
     * @return the piPeiWord
     */
    public short getMatchWord() {
        return matchWord;
    }
    public void setMatchWord(short matchWord) {
        this.matchWord = matchWord;
    }
}
