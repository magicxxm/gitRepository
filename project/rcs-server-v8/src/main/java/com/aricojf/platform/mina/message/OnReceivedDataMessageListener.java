/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

import com.aricojf.platform.mina.message.robot.RobotMessage;

/**
 *数据消息接口
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface OnReceivedDataMessageListener {   
    public void onReceivedMessage(RobotMessage data);
}
