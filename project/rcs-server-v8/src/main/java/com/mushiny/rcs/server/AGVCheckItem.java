/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

/**
 * AGV检查项目
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVCheckItem {

    private AGVMessage agv;
    //最后一次心跳时间
    private long lastBeatTime = 0;
    //最后一次实时数据包时间
    private long lastRTTime = 0;
    //最后一次位置改变时间
    private long lastPositionChanageTime = 0;
    //最后一次心跳或实时数据包时间
    private long lastBeatOrRTTime = 0;
    //最后一次锁格时间
    private long lastLockCellTime = 0;

    public AGVCheckItem(AGVMessage agv) {
        this.agv = agv;
        long t = System.currentTimeMillis();
        lastBeatOrRTTime = t;
        lastLockCellTime = t;
        lastPositionChanageTime = t;
        lastRTTime = t;
        lastBeatTime = t;
    }

    /**
     * @return the lastBeatTime
     */
    public long getLastBeatTime() {
        return lastBeatTime;
    }

    /**
     * @param lastBeatTime the lastBeatTime to set
     */
    public void setLastBeatTime(long lastBeatTime) {
        this.lastBeatTime = lastBeatTime;
    }

    /**
     * @return the lastRTTime
     */
    public long getLastRTTime() {
        return lastRTTime;
    }

    /**
     * @param lastRTTime the lastRTTime to set
     */
    public void setLastRTTime(long lastRTTime) {
        this.lastRTTime = lastRTTime;
    }

    /**
     * @return the lastPositionChanageTime
     */
    public long getLastPositionChanageTime() {
        return lastPositionChanageTime;
    }

    /**
     * @param lastPositionChanageTime the lastPositionChanageTime to set
     */
    public void setLastPositionChanageTime(long lastPositionChanageTime) {
        this.lastPositionChanageTime = lastPositionChanageTime;
        if (agv.isTaskTimeout()) {
            agv.setTaskTimeout(false);
        }
    }

    /**
     * @return the lastBeatOrRTTime
     */
    public long getLastBeatOrRTTime() {
        return lastBeatOrRTTime;
    }

    /**
     * @param lastBeatOrRTTime the lastBeatOrRTTime to set
     */
    public void setLastBeatOrRTTime(long lastBeatOrRTTime) {
        this.lastBeatOrRTTime = lastBeatOrRTTime;
//        ((KivaAGV) agv).setSendSPCountForTimeout(0);//把尝试路径的发送次数清0
        if (agv.isRtTimeout()) {
            agv.setRtTimeout(false);
            agv.setAGVStatus(agv.getPreviousAGVStatus());
        }
    }

    /**
     * @return the lastLockCellTime
     */
    public long getLastLockCellTime() {
        return lastLockCellTime;
    }

    /**
     * @param lastLockCellTime the lastLockCellTime to set
     */
    public void setLastLockCellTime(long lastLockCellTime) {
//        System.out.println("======================================设置锁格最后时间=" + lastLockCellTime);
        this.lastLockCellTime = lastLockCellTime;
        agv.setLockTimeout(false);
    }

}
