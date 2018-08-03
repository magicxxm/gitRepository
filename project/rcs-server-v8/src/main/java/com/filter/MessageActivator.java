package com.filter;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.message.robot.RCS2RobotActiveMessage;
import com.aricojf.platform.mina.message.robot.RobotMessage;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;
import org.apache.mina.core.session.IoSession;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 服务重新启动  小车直接发心跳或实时包时，给小车发激活包
 *
 * Created by Laptop-6 on 2017/8/30.
 * @author mingchun.mu@mushiny.com
 */
public class MessageActivator {

    Logger LOG = Logger.getLogger(MessageActivator.class.getName());

    private IMessageActivator messageActivator;
    private static Map<Long, Long> robotMap = new HashMap<>();

    public void doFilter(IoSession session, RobotMessage data){
        if (data != null) {
            data.toObject();
            long robotID = data.getRobotID();
            KivaAGV agv = AGVManager.getInstance().getAGVByID(robotID);
            if(agv == null){
                if(robotMap.get(robotID) == null){
                    robotMap.put(robotID, System.currentTimeMillis());
                    sendActiveMessage(session, robotID);
                }else{
                    if(System.currentTimeMillis() - robotMap.get(robotID) > AGVConfig.AGV_ACTIVE_INTERVAL_TIME){
                        robotMap.put(robotID, System.currentTimeMillis());
                        sendActiveMessage(session, robotID);
                    }
                }
            }
        }
        if(messageActivator != null){
            messageActivator.doFilter(session, data);
        }
    }

    private void sendActiveMessage(IoSession session, long robotID){
        RCS2RobotActiveMessage message = new RCS2RobotActiveMessage(robotID);
        message.toMessage();
//        LOG.info("发送激活信息 - - > >:"+ HexBinaryUtil.byteArrayToHexString2((byte[]) message.getMessage()));
        session.write(message.getMessage());
    }


    public IMessageActivator getMessageActivator() {
        return messageActivator;
    }

    public void setMessageActivator(IMessageActivator messageActivator) {
        this.messageActivator = messageActivator;
    }
}
