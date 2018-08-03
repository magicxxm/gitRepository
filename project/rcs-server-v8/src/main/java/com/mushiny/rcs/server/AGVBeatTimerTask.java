/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.mushiny.rcs.global.AGVConfig;
import java.util.TimerTask;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVBeatTimerTask extends TimerTask {

    private AGVManager agvManager;

    public AGVBeatTimerTask() {
        agvManager = AGVManager.getInstance();
    }

//    private int count = 0;
    public void run() {
        for (KivaAGV agv : agvManager.getAgvList()) {
            if (agv.isLogin()&&!agv.isRtTimeout()&&(agv.getAGVStatus()!=AGVConfig.AGV_STATUS_NO_CONNECTION)) {
                agv.sendBeatMessage();
            }
        }
    }
}
