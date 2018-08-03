/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;

/**
 *ROBOT连接到RCS SERVER消息
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotOpenConnectionMessage extends RobotMessage{
     public RobotOpenConnectionMessage() {
        this(-1);
    }

    public RobotOpenConnectionMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.messageBodyLength=0;
        this.frameDataLength = 9;
        this.functionWordCode = Robot2RCSMessageTypeConfig.ROBOT_CONNECTION_RCS_SERVER_OPEN;
    }
}
