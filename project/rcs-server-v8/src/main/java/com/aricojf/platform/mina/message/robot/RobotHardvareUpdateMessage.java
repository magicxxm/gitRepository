/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;

/**
 *固件更新数据包----------暂时不实现
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotHardvareUpdateMessage extends RobotMessage{
    
     public RobotHardvareUpdateMessage() {
       this(-1);
    }
     public RobotHardvareUpdateMessage(long robotID) {
        super();
        this.robotID=robotID;
        this.frameDataLength = 8;
        this.functionWordCode = RCS2RobotMessageTypeConfig.HARDWARE_UPDATE_MESSAGE;
        this.head = KivaBusConfig.ROBOT2RCS_HEAD;
    }
     
}
