/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.mingchun.mu.mushiny.kiva.pod.IPod;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.rcs.global.AGVConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * AGV检查定时器任务
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVCheckTimerTask extends TimerTask {

    private AGVManager agvManager;
    private AGVTimeoutManager agvTimeOutManager;
    static Logger LOG = LoggerFactory.getLogger(AGVCheckTimerTask.class.getName());

    public AGVCheckTimerTask() {
        agvManager = AGVManager.getInstance();
        agvTimeOutManager = AGVTimeoutManager.getInstance();
    }

    public void run() {
        try {
            timeCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timeCheck(){
        for (KivaAGV agv : agvManager.getAgvList()) {
            //1.检查AGV心跳是否超时
            if (agv.isLogin()) {
                if (System.currentTimeMillis() - agv.getAgvCheckItem().getLastBeatTime() > AGVConfig.AGV_BEAT_TIMEOUT) {
                    //--临时不需要 agvTimeOutManager.fireOnAGVBeatTimeout(agv);
                }
                //2.检查实时数据是否超时
                if (System.currentTimeMillis() - agv.getAgvCheckItem().getLastRTTime() > AGVConfig.AGV_RT_TIMEOUT) {
                    //--临时不需要 agvTimeOutManager.fireOnAGVRTTimeout(agv);
                }
                //3.检查心跳或实时数据是否超时
                if (System.currentTimeMillis() - agv.getAgvCheckItem().getLastBeatOrRTTime() > AGVConfig.AGV_BEAT_OR_RT_TIMEOUT
                        && agv.getAGVStatus() != AGVConfig.AGV_STATUS_NO_CONNECTION) {
                    LOG.error("####心跳、实时数据超时!!(AGV_ID=" + agv.getID());
                    agvTimeOutManager.fireOnAGVBeatOrRTTimeout(agv);
                    if (!agv.isRtTimeout()) {
                        agv.setRtTimeout(true);
                        agv.setAGVStatus(AGVConfig.AGV_STATUS_TIMEOUT);
                    }
                    agv.getAgvCheckItem().setLastBeatOrRTTime(System.currentTimeMillis());
                }

               /* //4.位置不改变超时
                if (agv.getAGVStatus() == AGVConfig.AGV_STATUS_TASKED
                        && ((System.currentTimeMillis() - agv.getAgvCheckItem().getLastPositionChanageTime()) > AGVConfig.AGV_NO_POSITION_CHANGE_TIMEOUT)) {
                    LOG.error("####任务超时!!(AGV_ID=" + agv.getID());

                    LOG.info("位置不改变时间-当前时间："+System.currentTimeMillis()+", 位置改变时间："+agv.getAgvCheckItem().getLastPositionChanageTime()+", agv超时时间间隔："+AGVConfig.AGV_NO_POSITION_CHANGE_TIMEOUT);


                    agv.setTaskTimeout(true);
                    agvTimeOutManager.fireOnAGVNoPositionChanageTimeout(agv);
                    //当遇到位置不改变超时，且有任务时，尝试发送路径任务
                    if (agv.getAGVStatus() == AGVConfig.AGV_STATUS_TASKED && agv.getSendSPCountForTimeout() < AGVConfig.AGV_SEND_SP_COUNTS_ON_TIMEOUT) {
                        agv.sendSeriesPath(agv.getCurrentSeriesPath());
                        agv.setSendSPCountForTimeout(agv.getSendSPCountForTimeout() + 1);

                        // mingchun.mu@mushiny.com -- 下发后更新最后一次位置不改变时间， 否则会5s发送一次。
                        agv.getAgvCheckItem().setLastPositionChanageTime(System.currentTimeMillis());

                    }
                }*/

                //4.位置不改变超时
                /**
                 * 位置不改变超时，小车的锁格路径已下发（包括自己以外的其他格子），但小车在指定的时间内没有走，需要人工处理后，重新下发路径，
                 * 并且会将当前的锁格下发的格子置为临时不可走点，当格子释放可走以后，上报。
                 */
                if (agv.getAGVStatus() == AGVConfig.AGV_STATUS_TASKED) {
//                    LOG.error("####任务超时!!(AGV_ID=" + agv.getID());
//                    LOG.info("位置不改变时间-当前时间："+System.currentTimeMillis()+", 位置改变时间："+agv.getAgvCheckItem().getLastPositionChanageTime()+", agv超时时间间隔："+AGVConfig.AGV_NO_POSITION_CHANGE_TIMEOUT);
                    if(agv.getLastSendedSeriesPath() != null
                            && agv.getLastSendedSeriesPath().getPathList() != null
                            && agv.getLastSendedSeriesPath().getPathList().size() > 1){
                        if(agv.getAgvCheckItem().getLastPositionChanageTime() > 0 && (System.currentTimeMillis() - agv.getAgvCheckItem().getLastPositionChanageTime()) > AGVConfig.AGV_NO_POSITION_CHANGE_TIMEOUT){
                            agv.setTaskTimeout(true);
                            Map<String, Object> paramMap = new HashMap<>();
                            List<Long> lockedCellList = new ArrayList<>();
                            List<CellNode> cellNodeList = new ArrayList<>(); // 更改cost值的list
                            for(CellNode cellNode: agv.getLastSendedSeriesPath().getPathList()){
                                lockedCellList.add(cellNode.getAddressCodeID());
                                cellNodeList.add(cellNode);
                            }
                            MapManager.getInstance().getMap().changingCellsCost(cellNodeList, true);
                            paramMap.put("lockedCellList", lockedCellList);
                            agvTimeOutManager.fireOnAGVNoPositionChanageTimeout(agv, paramMap);
                            agv.getAgvCheckItem().setLastPositionChanageTime(System.currentTimeMillis());
                        }

                        // 当遇到位置不改变超时，且有任务时，尝试发送路径任务
                        /*if (agv.getAgvCheckItem().getLastPositionChanageTime() > 0
                                && (System.currentTimeMillis() - agv.getAgvCheckItem().getLastPositionChanageTime()) > AGVConfig.AGV_NO_POSITION_CHANGE_TIMEOUT / 3
                                && agv.getSendSPCountForTimeout() < AGVConfig.AGV_SEND_SP_COUNTS_ON_TIMEOUT) {
                            if(agv.getCurrentSeriesPath() != null
                                    && agv.getCurrentSeriesPath().getPathList().size() > 0
                                    && agv.getCurrentSeriesPath().getPathList().getFirst().getAddressCodeID() == agv.getCurrentAddressCodeID()){
                                agv.sendSeriesPath(agv.getCurrentSeriesPath());
                                LOG.info("AGV("+agv.getID()+")位置不改变超时，重新下发路径："+agv.getCurrentSeriesPath());
                                agv.setSendSPCountForTimeout(agv.getSendSPCountForTimeout() + 1);
                            }
                            // mingchun.mu@mushiny.com -- 下发后更新最后一次位置不改变时间， 否则会5s发送一次。
//                            agv.getAgvCheckItem().setLastPositionChanageTime(System.currentTimeMillis()); // 实时包更新
                        }*/
                    }
                }

                //5.锁格超时
                /**
                 * 锁格超时，小车除自己外无法锁格下发，且等待时间超过指定时间时，通知上报，
                 * 上报下一个不可锁的格子，如果此格是其他的车占用，将此格视为临时不可走点，解锁后上报；
                 * 如果此格是被pod占用，就不用设置为临时不可走点。
                 */
                if (agv.getAGVStatus() == AGVConfig.AGV_STATUS_TASKED) {
//                    LOG.debug("-----------------timeA=" + (System.currentTimeMillis() - agv.getAgvCheckItem().getLastLockCellTime()));
//                    LOG.debug("-----------------timeB=" + AGVConfig.AGV_LOCK_SP_TIMEOUT);
                    if(agv.getLastSendedSeriesPath() != null
                            && agv.getLastSendedSeriesPath().getPathList() != null
                            && agv.getLastSendedSeriesPath().getPathList().size() == 1){ // 锁格超时，表示有任务却不能下发除自己之外的其他格子
                        if (agv.getAgvCheckItem().getLastLockCellTime() > 0 && ((System.currentTimeMillis() - agv.getAgvCheckItem().getLastLockCellTime()) > AGVConfig.AGV_LOCK_SP_TIMEOUT)) {
                            Map<String, Object> paramMap = new HashMap<>();
                            long waitingLockAddressCodeID = 0;
                            if(agv.getCurrentGlobalSeriesPath() != null
                                    && agv.getCurrentGlobalSeriesPath().getPathList() != null
                                    && agv.getCurrentGlobalSeriesPath().getPathList().size() > 1){
                                waitingLockAddressCodeID = agv.getCurrentGlobalSeriesPath().getPathList().get(1).getAddressCodeID();
                            }
                            paramMap.put("waitingLockAddressCodeID", waitingLockAddressCodeID);
                            CellNode waitingLockCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(waitingLockAddressCodeID);
                            IPod pod = PodManager.getInstance().getPod(waitingLockCellNode);
                            if(pod != null){
                                paramMap.put("nextLockAddressPodCodeID", pod.getPodCodeID());
                            }
                            if(waitingLockCellNode != null
                                    && !waitingLockCellNode.isUnlockedTimeout()
                                    && (!agv.equals(waitingLockCellNode.getNowLockedAGV()))){
                                LOG.warn("####锁格超时!!(AGV_ID=" + agv.getID());
                                agv.setLockTimeout(true);

                                /*agv.clearAndUnlockBufferSP();
                                LOG.info("小车("+agv.getID()+")重新请求，清除所有缓冲路径！！！");

                                agvTimeOutManager.fireOnAGVLockCellTimeout(agv, paramMap);
                                LOG.info("AGV("+agv.getID()+")锁格超时重新请求路径！！！");


                                List<CellNode> cellNodeList = new ArrayList<>();
                                cellNodeList.add(MapManager.getInstance().getMap().getMapCellByAddressCodeID(waitingLockAddressCodeID));
                                cellNodeList.add(MapManager.getInstance().getMap().getMapCellByAddressCodeID(agv.getCurrentAddressCodeID()));
                                MapManager.getInstance().getMap().changingCellsCost(cellNodeList, true);
*/

//                                agv.notifyLockTimeout();
//                                LOG.info("AGV("+agv.getID()+")清除相关缓冲路径！！！");


//                                if(pod == null){
//                                }
                            }
//                            agv.getAgvCheckItem().setLastLockCellTime(System.currentTimeMillis());
                        }else {
//                            LOG.debug("-----------------锁格超时吗？不超时.");
                        }
                    }

                } else {
//                    LOG.debug("-----------------AGV非任务状态，不需要检查锁格超时！！");
                }
            }
        }
    }


}
