/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.server;

import com.aricojf.platform.mina.common.MinaConfig;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *Server端的被动型心跳
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ServerKeepAliveMessageFactoryImp implements KeepAliveMessageFactory {
    static Logger LOG = LoggerFactory.getLogger(ServerKeepAliveMessageFactoryImp.class.getName());
    long bTime = 0;
    long eTime = 0;
    //判断是否心跳请求包  是的话返回true 
    public boolean isRequest(IoSession session, Object message) {
        if(message instanceof String && message.equals(MinaConfig.PING_MESSAGE)){
            eTime = System.currentTimeMillis();
            LOG.info("Server收到心跳包(请求)...时间间隔="+(eTime-bTime)/1000);
            bTime = eTime;
            return true;
        }
        return false;
    }
    //由于被动型心跳机制，没有请求当然也就不关注反馈 因此直接返回false
    public boolean isResponse(IoSession session, Object message) {      
        return false;
    }
    //被动型心跳机制无请求  因此直接返回null
    public Object getRequest(IoSession session) {
        return null;
    }
    //根据心跳请求request 反回一个心跳反馈消息
    public Object getResponse(IoSession session, Object request) {
        return MinaConfig.PONG_MESSAGE;
    }
}
