/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.server;

import com.mushiny.rcs.server.AGVManager;
import com.aricojf.platform.common.Global;
import com.mushiny.rcs.global.AGVConfig;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import com.mushiny.rcs.server.KivaAGV;
import com.mushiny.rcs.server.RCSStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class MinaServerOperation extends IoHandlerAdapter {

    static Logger LOG = LoggerFactory.getLogger(MinaServerOperation.class.getName());

    public MinaServerOperation() {
        super();
    }

    public void messageReceived(IoSession session, Object message) {

    }

    public void messageSent(IoSession session, Object message) {
    }

    public void sessionCreated(IoSession session) {
    }

    /*
     新客户端连接打开本SERVER
     */
    public void sessionOpened(IoSession session) throws Exception {
        KivaAGV robot = new KivaAGV();
        robot.setSession(session);
        AGVManager.getInstance().notifyAGVOpenConnection(robot);
        String log = "";
        log += Global.LINE + "\r\n";
        log += Global.LogCommonPrefix + "新AGV连接到本Server事件..." + "\r\n";
        log += Global.LogCommonPrefix + "sessionID=" + session.getId() + "\r\n";
        log += Global.LogCommonPrefix + "address=" + session.getRemoteAddress() + "\r\n";
        log += Global.LogCommonPrefix + "目前AGV数量=" + AGVManager.getInstance().getAGVCount() + "\r\n";
        log += Global.LINE + "\r\n";
        RCSStatusService.getInstance().postServerStatusMessage(log);
        LOG.debug(log);
    }

    public void sessionClosed(IoSession session) throws Exception {
        KivaAGV robot = AGVManager.getInstance().getAGVBySession(session);
        if (robot == null) {
            return;
        }
        if(robot.getAGVStatus()==AGVConfig.AGV_STATUS_NO_CONNECTION){
            return;
        }
        robot.setAGVStatusOnTCPConnectionClose();//无TCP连接
        AGVManager.getInstance().notifyAGVCloseConnection(robot);
        String log = "";
        log += Global.LINE + "\r\n";
        log += Global.LogCommonPrefix + "AGV(AGV_ID=" + robot.getID() + ")与本SERVER断开事件...." + "\r\n";
        log += Global.LogCommonPrefix + "session id=" + session.getId() + "\r\n";
        log += Global.LogCommonPrefix + "address=" + session.getRemoteAddress() + "\r\n";
        log += Global.LogCommonPrefix + "目前注册AGV数量=" + AGVManager.getInstance().getAGVCount() + "\r\n";
        log += Global.LINE + "\r\n";
        RCSStatusService.getInstance().postServerStatusMessage(log);
        session.close();
        LOG.debug(log);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

    }

    /*
     客户端不经过关闭socket,而是直接退出通常会触发此方法
     */
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        KivaAGV robot = AGVManager.getInstance().getAGVBySession(session);
        if (robot == null) {
            return;
        }
        if(robot.getAGVStatus()==AGVConfig.AGV_STATUS_NO_CONNECTION){
            robot.setAGVStatusOnTCPConnectionClose();//无TCP连接
            return;
        }
        AGVManager.getInstance().notifyAGVCloseConnection(robot);
        String log = "AGV连接断开，  IP=" + session.getRemoteAddress() + "，AGV_ID=" + robot.getID() + ",原因：" + cause.getLocalizedMessage();
        RCSStatusService.getInstance().postServerStatusMessage(log);
        session.close();
         LOG.debug(log);
    }
}
