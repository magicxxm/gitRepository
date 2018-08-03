/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseExceptionMessage;
import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseMessage;
import com.aricojf.platform.mina.server.ServerManager;
import com.mingchun.mu.aricojf.platform.mina.message.robot.media.error.Robot2RCSMidiaErrorMessage;

/**
 * AGV消息处理器
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class DefaultAGVReceiveMessageProcessor implements OnReceiveAGVAllMessageListener {

    private RobotRTMessage rtMessage;
    private RobotHeartBeatRequestMessage heartMessage;
    private RobotStatusMessage statusMessage;
    private RobotLoginRequestMessage loginMessage;
    private RobotErrorMessage errorMessage;
    private Robot2RCSActionCommandResponseMessage actionCommandReponseMessage;

    public DefaultAGVReceiveMessageProcessor() {
        rtMessage = new RobotRTMessage();
        heartMessage = new RobotHeartBeatRequestMessage();
        statusMessage = new RobotStatusMessage();
        loginMessage = new RobotLoginRequestMessage();
        errorMessage = new RobotErrorMessage();
        actionCommandReponseMessage = new Robot2RCSActionCommandResponseMessage();
        ServerManager.getMessageServerInstance().registeAGVAllMessageListener(this);
    }

    public void onReceivedAGVRTMessage(RobotRTMessage data) {
        rtMessage = data;
    }

    public void onReceivedAGVHeartBeatMessage(RobotHeartBeatRequestMessage data) {
        heartMessage = data;
    }

    public void onReceivedAGVStatusMessage(RobotStatusMessage data) {
        statusMessage = data;
    }

    public void onReceivedAGVErrorMessage(RobotErrorMessage data) {
        errorMessage = data;
    }

    public void onReceivedAGVLoginMessage(RobotLoginRequestMessage data) {
        loginMessage = data;
    }
    public void onReceivedAGVActionCommandResponseMessage(Robot2RCSActionCommandResponseMessage data){
        actionCommandReponseMessage = data;
    }

    public RobotRTMessage getRtMessage() {
        return rtMessage;
    }
    public RobotHeartBeatRequestMessage getHeartMessage() {
        return heartMessage;
    }
    public RobotStatusMessage getStatusMessage() {
        return statusMessage;
    }
    public RobotLoginRequestMessage getLoginMessage() {
        return loginMessage;
    }
    public RobotErrorMessage getErrorMessage() {
        return errorMessage;
    }


    @Override
    public void onReceiveRobot2RCSActionFinishedMessageListener(Robot2RCSActionFinishedMessage data) {

    }

    @Override
    public void OnReceiveRobot2RCSResponseMessage(Robot2RCSResponseMessage data) {

    }

    @Override
    public void OnReceiveRobot2RCSResponseExceptionMessage(Robot2RCSResponseExceptionMessage data) {

    }

    @Override
    public void onReceiveRobot2RCSMediaErrorMessageListener(Robot2RCSMidiaErrorMessage data) {

    }

    @Override
    public void onReceiveRobot2RCSResponseConfigMessageListener(RobotResponseConfigMessage data) {

    }
}
