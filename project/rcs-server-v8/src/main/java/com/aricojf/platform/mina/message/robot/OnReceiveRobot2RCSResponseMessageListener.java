package com.aricojf.platform.mina.message.robot;


import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseMessage;

/**
 *
 * 收到agv路径应答包监听
 *
 */
public interface OnReceiveRobot2RCSResponseMessageListener {
    void OnReceiveRobot2RCSResponseMessage(Robot2RCSResponseMessage data);
}
