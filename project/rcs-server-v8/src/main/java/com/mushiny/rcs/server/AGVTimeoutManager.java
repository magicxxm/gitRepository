/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.listener.AGVTimeoutListener;
import java.util.LinkedList;
import java.util.Map;


/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVTimeoutManager {

    private final LinkedList<AGVTimeoutListener> agvTimeoutListenerList = new LinkedList();
    private static AGVTimeoutManager instance;

    private AGVTimeoutManager() {

    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new AGVTimeoutManager();
        }
    }

    public static AGVTimeoutManager getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }
/*
    public static AGVTimeoutManager getInstance() {
        if (instance == null) {
            instance = new AGVTimeoutManager();
        }
        return instance;
    }
*/

    public void registeAGVTimeoutListener(AGVTimeoutListener listener) {
        if (listener == null) {
            return;
        }
        if (!agvTimeoutListenerList.contains(listener)) {
            agvTimeoutListenerList.addLast(listener);
        }
    }

    public void removeAGVTimeoutListener(AGVTimeoutListener listener) {
        if (listener == null) {
            return;
        }
        if (agvTimeoutListenerList.contains(listener)) {
            agvTimeoutListenerList.remove(listener);
        }
    }

    public void fireOnAGVBeatTimeout(KivaAGV agv) {
        for (AGVTimeoutListener listener : agvTimeoutListenerList) {
            listener.onAGVBeatTimeout(agv);
        }
    }

    public void fireOnAGVRTTimeout(KivaAGV agv) {
        for (AGVTimeoutListener listener : agvTimeoutListenerList) {
            listener.onAGVRTTimeout(agv);
        }
    }

    public void fireOnAGVBeatOrRTTimeout(KivaAGV agv) {
        for (AGVTimeoutListener listener : agvTimeoutListenerList) {
            listener.onAGVBeatOrRTTimeout(agv);
        }
    }

    public void fireOnAGVNoPositionChanageTimeout(KivaAGV agv, Map<String, Object> paramMap) {
        if (agv.getAGVStatus() == AGVConfig.AGV_STATUS_TASKED) {
            for (AGVTimeoutListener listener : agvTimeoutListenerList) {
                listener.onAGVPositionNoChanageTimeout(agv, paramMap);
            }
        }
    }

    public void fireOnAGVLockCellTimeout(KivaAGV agv, Map<String, Object> paramMap) {
//        if (agv.getAGVStatus() == AGVConfig.AGV_STATUS_TASKED) {
            for (AGVTimeoutListener listener : agvTimeoutListenerList) {
                listener.onAGVLockCellTimeout(agv, paramMap);
                //--agv.onAGVLockCellTimeout();
            }
//        }
    }
}
