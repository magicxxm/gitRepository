/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.server;

/**
 *SERVER实例管理
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ServerManager {
    private static ServerManager serverManagerInstance;
    private static ServerMessageService serviceInstance;
    private ServerManager() {
    }
//    public static ServerManager getInstance() {
//        if(serverManagerInstance==null) {
//           serverManagerInstance = new ServerManager();
//        }
//        return serverManagerInstance;
//    }
    public static ServerMessageService getMessageServerInstance() {
       return ServerMessageService.getInstance();
    }
}
