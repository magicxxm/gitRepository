/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;

/**
 * 车辆登录回复包
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCS2RobotLoginResponseMessage extends RobotMessage {

    public RCS2RobotLoginResponseMessage() {
        this(-1);
    }

    public RCS2RobotLoginResponseMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength = 0;
        this.frameDataLength = 9;
        this.functionWordCode = RCS2RobotMessageTypeConfig.ROBOT_LOGIN_RRESPONSE_MESSAGE;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;

    }
}
