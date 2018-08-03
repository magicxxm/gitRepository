/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

/**
 *服务器状态消息接口
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface ServerStatusChanagedMessageListener {
    public void onServerStatusChanaged(ServerStatusMessage status);
}
