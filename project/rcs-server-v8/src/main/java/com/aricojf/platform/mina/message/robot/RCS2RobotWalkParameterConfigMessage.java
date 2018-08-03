/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;

/**
 * 行走参数配置包
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCS2RobotWalkParameterConfigMessage extends RobotMessage{
    private int weightClass; // 重量等级 -- 1字节
    private int reservedWord; // 保留字 -- 4字节
    public RCS2RobotWalkParameterConfigMessage() {
        this(-1);
    }
    public RCS2RobotWalkParameterConfigMessage(long robotID) {
         super();
        this.robotID = robotID;
        this.messageBodyLength=5;
        this.frameDataLength=14;
        this.functionWordCode=RCS2RobotMessageTypeConfig.WALK_PARAMETER_CONFIG_MESSAGE;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;
    }

    @Override
    public void toMessage() {
        byte[] tmpMessageBody = new byte[messageBodyLength];
        //故障ID
        tmpMessageBody[0] = (byte) (weightClass & 0x00ff);

        // 保留字

        setMessageBody(tmpMessageBody);
        super.toMessage();
    }

    public int getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(int weightClass) {
        this.weightClass = weightClass;
    }

    public int getReservedWord() {
        return reservedWord;
    }

    public void setReservedWord(int reservedWord) {
        this.reservedWord = reservedWord;
    }
}
