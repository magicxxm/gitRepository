/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.listener;

import com.aricojf.platform.mina.message.robot.Robot2RCSActionCommandResponseMessage;
import com.aricojf.platform.mina.message.robot.RobotErrorMessage;
import com.aricojf.platform.mina.message.robot.RobotHeartBeatRequestMessage;
import com.aricojf.platform.mina.message.robot.RobotLoginRequestMessage;
import com.aricojf.platform.mina.message.robot.RobotRTMessage;
import com.aricojf.platform.mina.message.robot.RobotStatusMessage;
import com.mushiny.rcs.server.AGVMessage;
import com.mushiny.rcs.server.KivaAGV;

/**
 *以AGV为中心
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface AGVDataListener {

    public void onReceivedRTMessage(AGVMessage agv,RobotRTMessage rtMessage);

    public void onReceivedHeartBeatMessage(AGVMessage agv,RobotHeartBeatRequestMessage heartMessage);

    public void onReceivedStatusMessage(AGVMessage agv,RobotStatusMessage statusMessage);

    public void onReceivedErrorMessage(AGVMessage agv,RobotErrorMessage errorMessage);

    public void onReceivedLoginMessage(AGVMessage agv,RobotLoginRequestMessage loginMessage);

    public void onReceivedActionCommandResponseMessage(AGVMessage agv,Robot2RCSActionCommandResponseMessage actionCommandReponseMessage);
}
