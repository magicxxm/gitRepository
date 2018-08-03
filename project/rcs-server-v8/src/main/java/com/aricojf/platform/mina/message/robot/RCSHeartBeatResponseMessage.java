/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;

/**
 * 心跳数据包-服务器响应
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCSHeartBeatResponseMessage extends RobotMessage {

    public RCSHeartBeatResponseMessage() {
        this(-1);
    }

    public RCSHeartBeatResponseMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength=0;
        this.frameDataLength = 9;//心跳包帧长为9
        this.functionWordCode = RCS2RobotMessageTypeConfig.HEART_BEAT_RESPONSE_MESSAGE;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;
    }

}
