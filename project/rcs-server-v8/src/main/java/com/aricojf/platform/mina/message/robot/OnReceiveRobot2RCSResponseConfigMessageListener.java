package com.aricojf.platform.mina.message.robot;


/**
 *
 * 收到agv路径应答包监听
 *
 */
public interface OnReceiveRobot2RCSResponseConfigMessageListener {
    void onReceiveRobot2RCSResponseConfigMessageListener(RobotResponseConfigMessage data);
}
