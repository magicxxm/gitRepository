/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

/**
 *直行路径包
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RobotDirectPathMessage extends RCS2RobotPathMessage{
    public RobotDirectPathMessage() {
        super();
    }
    public RobotDirectPathMessage(long robotID) {
          super(robotID);
    }
}
