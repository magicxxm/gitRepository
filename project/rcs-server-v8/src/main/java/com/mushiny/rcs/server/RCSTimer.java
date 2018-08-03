/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.mushiny.rcs.global.AGVConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

/**
 *RCS定时任务
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCSTimer   {
    private static Logger LOG = LoggerFactory.getLogger(RCSTimer.class.getName());
    private static RCSTimer instance;
    private Timer  AGVCheckTimer;
    private Timer  AGVBeatTimerTask;
    private boolean isRun = false;
    private RCSTimer() {
        isRun=false;
    }
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new RCSTimer();
        }
    }
    public static  RCSTimer getInstance() {
        if(instance==null) {
            initInstance();
        }
        return instance;
    }
/*
    public static  RCSTimer getInstance() {
        if(instance==null) {
            instance = new RCSTimer();
        }
        return instance;
    }
*/
    public  void start() {
        if(isRun) {
            return;
        }
        AGVCheckTimer = new Timer("AGV检查定时器");
        getAGVCheckTimer().scheduleAtFixedRate(new AGVCheckTimerTask(),1, AGVConfig.AGV_CHECK_LOOP_TIME);
        //AGV心跳
        AGVBeatTimerTask = new Timer("AGV心跳定时器");
        getAGVBeatTimerTask().scheduleAtFixedRate(new AGVBeatTimerTask(), 5, AGVConfig.AGV_BEAT_LOOP_TIME);
        isRun = true;
    }

    /**
     * @return the AGVCheckTimer
     */
    public Timer getAGVCheckTimer() {
        return AGVCheckTimer;
    }

    /**
     * @return the AGVBeatTimerTask
     */
    public Timer getAGVBeatTimerTask() {
        return AGVBeatTimerTask;
    }
    
    
   

    
}
