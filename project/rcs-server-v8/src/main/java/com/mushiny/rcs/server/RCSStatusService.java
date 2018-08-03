/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.aricojf.platform.mina.message.ServerStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RCS状态服务
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCSStatusService extends RCSStatusContainer implements Runnable {

    static Logger LOG = LoggerFactory.getLogger(RCSStatusService.class.getName());
    private ServerStatusMessage statusMessage;
    private static RCSStatusService instance;

    private RCSStatusService() {

    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new RCSStatusService();
            new Thread(instance).start();
        }
    }

    public static RCSStatusService getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }
/*
    public static RCSStatusService getInstance() {
        if (instance == null) {
            instance = new RCSStatusService();
            new Thread(instance).start();
        }
        return instance;
    }
*/

    public void run() {
        while (true) {            
            statusMessage = getServerStatusMessage();
            if (statusMessage != null) {
                fireServerStatusMessage(statusMessage);
            }
        }
    }
}
