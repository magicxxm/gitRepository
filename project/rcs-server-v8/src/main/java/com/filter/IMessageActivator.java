package com.filter;

import com.aricojf.platform.mina.message.robot.RobotMessage;
import org.apache.mina.core.session.IoSession;

/**
 * Created by Laptop-6 on 2017/8/30.
 */
public interface IMessageActivator {

    public void doFilter(IoSession session, RobotMessage data);



}
