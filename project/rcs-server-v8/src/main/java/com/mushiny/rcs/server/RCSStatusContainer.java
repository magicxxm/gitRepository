/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.aricojf.platform.mina.message.ServerStatusMessage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import com.aricojf.platform.mina.message.ServerStatusChanagedMessageListener;
import com.mingchun.mu.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  服务器状态容器
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCSStatusContainer  {
    private Logger LOG = LoggerFactory.getLogger(RCSStatusContainer.class.getName());
    //服务器状态消息缓冲区
    protected LinkedBlockingQueue<ServerStatusMessage> statusMessageBufferList = new LinkedBlockingQueue();
    //服务器状态消息监听
    private final List<ServerStatusChanagedMessageListener> statusMessageListernList = Collections.synchronizedList(new LinkedList());
    //注册服务器状态消息监听
    public void registerRCSStatusListener(ServerStatusChanagedMessageListener listener) {
        if (!statusMessageListernList.contains(listener)) {
            statusMessageListernList.add(listener);
        }
    }

    //移除服务器状态消息监听
    public void removeRCSStatusListener(ServerStatusChanagedMessageListener listener) {
        if (statusMessageListernList.contains(listener)) {
            statusMessageListernList.remove(listener);
        }
    }
    
     //缓冲数据入栈
    public void postServerStatusMessage(ServerStatusMessage data) {
        if(data==null) {
            LOG.error("####严重错误,RCS缓冲数据压栈时出错,消息为空!");
            return;
        }
        try {
            statusMessageBufferList.put(data);
        } catch (InterruptedException ie) {
            LOG.error("####严重错误,RCS缓冲数据压栈时被中断:\n" + ExceptionUtil.getMessage(ie));
        }
    }
      //缓冲数据入栈
    public void postServerStatusMessage(String log) {
        ServerStatusMessage data = new ServerStatusMessage();
        data.setMessage(log);
        try {
            statusMessageBufferList.put(data);
        } catch (InterruptedException ie) {
            LOG.error("####严重错误,RCS缓冲数据压栈时被中断:\n" + ExceptionUtil.getMessage(ie));
        }
    }

    //从缓冲数据得到消息
    public ServerStatusMessage getServerStatusMessage() {
        try {
            return statusMessageBufferList.take();
        } catch (InterruptedException ie) {
            LOG.error("####严重错误,RCS缓冲数据GET时被中断:\n" + ExceptionUtil.getMessage(ie));
            return null;
        }
    }
   
      //分发服务器状态消息
    public void fireServerStatusMessage(ServerStatusMessage status) {
        for (ServerStatusChanagedMessageListener listener : statusMessageListernList) {
            listener.onServerStatusChanaged(status);
        }
    }
   
}
