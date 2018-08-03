package com.aricojf.platform.mina.message.robot;


import com.mingchun.mu.aricojf.platform.mina.message.robot.media.error.Robot2RCSMidiaErrorMessage;

/**
 *
 * 收到agv路径应答包监听
 *
 */
public interface OnReceiveRobot2RCSMediaErrorMessageListener {
    void onReceiveRobot2RCSMediaErrorMessageListener(Robot2RCSMidiaErrorMessage data);
}
