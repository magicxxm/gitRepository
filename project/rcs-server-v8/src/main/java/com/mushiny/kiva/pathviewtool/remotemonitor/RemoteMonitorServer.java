/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.pathviewtool.remotemonitor;

import com.mushiny.rcs.global.RCSConfig;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RemoteMonitorServer {

    private static RemoteMonitorServer instance;
    private boolean run;
    private BufferedImage viewArea;
    private RemoteMonitorServer() {
    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new RemoteMonitorServer();
        }
    }

    public static RemoteMonitorServer getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }

    public void startServer() {
        if (run) {
            return;
        }
        try {
              ServerSocket serverSocket = new ServerSocket(RCSConfig.REMOTE_MONITOR_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                BroadcastMonitorContent monitor = new BroadcastMonitorContent(dos);
                new Thread(monitor).start();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void stopServer() {

    }

    public BufferedImage getViewArea() {
        return viewArea;
    }
    public void setViewArea(BufferedImage viewArea) {
        this.viewArea = viewArea;
    }

}
