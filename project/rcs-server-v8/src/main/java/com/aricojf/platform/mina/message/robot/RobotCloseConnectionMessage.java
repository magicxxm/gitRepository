/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;

/**
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotCloseConnectionMessage extends RobotMessage{
     public RobotCloseConnectionMessage() {
        this(-1);
    }

    public RobotCloseConnectionMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength=0;
        this.frameDataLength = 9;
        this.functionWordCode = Robot2RCSMessageTypeConfig.ROBOT_CONNECTION_RCS_SERVER_CLOSE;
    }
}
