/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ServerKeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler{
    private static Logger LOG = LoggerFactory.getLogger(ServerKeepAliveRequestTimeoutHandlerImpl.class.getName());
    public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
        LOG.warn("Server心跳超时！！！！");
    }
}
