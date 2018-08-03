package com.mushiny.rcs.server;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.message.robot.RobotMessage;
import com.aricojf.platform.mina.server.MinaServerOperation;
import com.aricojf.platform.mina.server.ServerManager;
import com.filter.LoginRequestMessageIntervalGetter;
import com.filter.MessageActivator;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 *  消息接收处理器
 */
public class RCSMessageOperation extends MinaServerOperation {

    private Logger LOG = LoggerFactory.getLogger(RCSMessageOperation.class.getName());
    KivaAGV agv;

    public RCSMessageOperation() {
        super();
    }

    private MessageActivator messageActivator = new MessageActivator();
    private LoginRequestMessageIntervalGetter loginRequestMessageIntervalGetter = new LoginRequestMessageIntervalGetter();

    public void messageReceived(IoSession session, Object message) {

        RobotMessage robotMessage = new RobotMessage();
        robotMessage.setMessage((byte[])message);

        messageActivator.doFilter(session, robotMessage);
        agv = AGVManager.getInstance().getAGVBySession(session);
        if (agv != null) {
            robotMessage.setMachine(agv);
            robotMessage.setSession(session);
            robotMessage.toObject();

//            LOG.info("线程："+ServerManager.getMessageServerInstance().serverMessageServiceThread.getName()+", 状态："+ServerManager.getMessageServerInstance().serverMessageServiceThread.getState());

            InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
            if(address != null ) {
                MDC.put("session", agv.getID()+"/"+address.getAddress()+"-"+address.getPort());
                LOG.info(" < < < - -  收到AGV("+agv.getID()+")报文(session="+session+") :"+ HexBinaryUtil.byteArrayToHexString2((byte[]) robotMessage.getMessage()));
            }

            if (robotMessage.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_LOGIN_REQUEST_MESSAGE) {
                if(loginRequestMessageIntervalGetter.handle(robotMessage)){
                    ServerManager.getMessageServerInstance().postReceiveDataMessage(robotMessage);
                }
            }else{
                ServerManager.getMessageServerInstance().postReceiveDataMessage(robotMessage);
            }
        } else {
            LOG.error("找不到对应消息的AGV!!!  sessionID="+session.getId());
        }
    }
}
