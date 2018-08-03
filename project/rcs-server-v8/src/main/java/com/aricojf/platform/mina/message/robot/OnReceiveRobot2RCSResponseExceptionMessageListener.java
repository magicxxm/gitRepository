package com.aricojf.platform.mina.message.robot;

import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseExceptionMessage;

/**
 *
 * 收到agv路径应答包监听
 *
 */
public interface OnReceiveRobot2RCSResponseExceptionMessageListener {
    void OnReceiveRobot2RCSResponseExceptionMessage(Robot2RCSResponseExceptionMessage data);
}
