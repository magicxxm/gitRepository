/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.listener;

import com.aricojf.platform.mina.message.OnReceivedDataMessageListener;
import com.aricojf.platform.mina.message.robot.OnReceiveAGVActionCommandResponseMessageListener;
import com.aricojf.platform.mina.message.robot.OnReceiveAGVAllMessageListener;
import com.aricojf.platform.mina.message.robot.OnReceiveAGVErrorMessageListener;
import com.aricojf.platform.mina.message.robot.OnReceiveAGVHeartBeatMessageListener;
import com.aricojf.platform.mina.message.robot.OnReceiveAGVLoginMessageListener;
import com.aricojf.platform.mina.message.robot.OnReceiveAGVRTMessageListener;
import com.aricojf.platform.mina.message.robot.OnReceiveAGVStatusMessageListener;
import com.aricojf.platform.mina.server.ServerMessageService;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *RCS接口管理器
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCSListenerManager {
    private static RCSListenerManager instance;
    private ServerMessageService messageServiceInstance;
    private List<AGVDataListener> agvDataListenerList = new CopyOnWriteArrayList();
    private RCSListenerManager () {
        messageServiceInstance = ServerMessageService.getInstance();
    }
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new RCSListenerManager();
        }
    }
    public static RCSListenerManager getInstance() {
        if(instance==null) {
            initInstance();
        }
        return instance;
    }
    /*public static RCSListenerManager getInstance() {
        if(instance==null) {
            instance = new RCSListenerManager();
        }
        return instance;
    }*/
    //---------------AGVListener监听------------------
    public void registeAGVListener(AGVListener listener) {
        AGVManager.getInstance().registeAGVListener(listener);
    }
    public void removeAGVListener(AGVListener listener) {
        AGVManager.getInstance().removeAGVListener(listener);
    }
    //---------------AGVTimeout监听---------------------
    public void registeAGVTimeoutListener(AGVTimeoutListener listener) {
        AGVManager.getInstance().registeAGVTimeoutListener(listener);
    }
    public void removeAGVTimeoutListener(AGVTimeoutListener listener) {
        AGVManager.getInstance().removeAGVTimeoutListener(listener);
    }
    //--------------CellListener监听----------------------
    public void registeCellListener(CellListener listener) {
        MapManager.getInstance().getMap().registeCellListener(listener);
    }
    public void removeCellListener(CellListener listener) {
        MapManager.getInstance().getMap().removeCellListener(listener);
    }
    //--------------AGV2CellListener监听------------------
    public void registeAGV2CellListener(AGV2CellListener listener) {
        MapManager.getInstance().getMap().regsiteAGV2CellListener(listener);
    }
     public void removeAGV2CellListener(AGV2CellListener listener) {
        MapManager.getInstance().getMap().removeAGV2CellListener(listener);
    }
     //===========================监听注册========================================
     //注册报文消息监听
    public void registeReceiveDataMessageListener(OnReceivedDataMessageListener listener) {
       messageServiceInstance.registeReceiveDataMessageListener(listener);
    }

    //移除报文消息监听
    public void removeReceiveDataMessageListener(OnReceivedDataMessageListener listener) {
       messageServiceInstance.removeReceiveDataMessageListener(listener);
    }
    public void registeAGVAllMessageListener(OnReceiveAGVAllMessageListener listener) {
       messageServiceInstance.registeAGVAllMessageListener(listener);
    }

    public void removeAGVAllMessageListener(OnReceiveAGVAllMessageListener listener) {
       messageServiceInstance.removeAGVAllMessageListener(listener);
    }

    public void registerAGVRTMessageListener(OnReceiveAGVRTMessageListener listener) {
        messageServiceInstance.registeAGVRTMessageListener(listener);
    }

    public void removeAGVRTMessageListener(OnReceiveAGVRTMessageListener listener) {
        messageServiceInstance.removeAGVRTMessageListener(listener);
    }

    public void registeAGVStatusMessageListener(OnReceiveAGVStatusMessageListener listener) {
         messageServiceInstance.registeAGVStatusMessageListener(listener);
    }

    public void removeAGVStatusMessageListener(OnReceiveAGVStatusMessageListener listener) {
        messageServiceInstance.removeAGVStatusMessageListener(listener);
    }

    public void registeAGVHeartMessageListener(OnReceiveAGVHeartBeatMessageListener listener) {
        messageServiceInstance.registeAGVHeartMessageListener(listener);
    }

    public void removeAGVHeartMessageListener(OnReceiveAGVHeartBeatMessageListener listener) {
         messageServiceInstance.removeAGVHeartMessageListener(listener);
    }

    public void registerAGVErrorMessageListener(OnReceiveAGVErrorMessageListener listener) {
        messageServiceInstance.registeAGVErrorMessageListener(listener);
    }

    public void removeAGVErrorMessageListener(OnReceiveAGVErrorMessageListener listener) {
        messageServiceInstance.removeAGVErrorMessageListener(listener);
    }

    public void registerAGVLoginMessageListener(OnReceiveAGVLoginMessageListener listener) {
        messageServiceInstance.registerAGVLoginMessageListener(listener);
    }

    public void removeAGVLoginMessageListener(OnReceiveAGVLoginMessageListener listener) {
        messageServiceInstance.removeAGVLoginMessageListener(listener);
    }
    public void registeOnReceiveAGVActionCommandResponseMessageListener(OnReceiveAGVActionCommandResponseMessageListener listener) {
       messageServiceInstance.registeOnReceiveAGVActionCommandResponseMessageListener(listener);
    }
     public void removeOnReceiveAGVActionCommandResponseMessageListener(OnReceiveAGVActionCommandResponseMessageListener listener) {
       messageServiceInstance.removeOnReceiveAGVActionCommandResponseMessageListener(listener);
    }
     //------------------------AGVDataListener--------------------------------------------
     public void registeAGVDataListener(AGVDataListener listener) {
         if(!agvDataListenerList.contains(listener)) {
             getAgvDataListenerList().add(listener);
             for(KivaAGV agv:AGVManager.getInstance().getAgvList()) {
                 agv.registeAGVDataListener(listener);
             }
         }
     }
      public void removeAGVDataListener(AGVDataListener listener) {
         if(getAgvDataListenerList().contains(listener)) {
             getAgvDataListenerList().remove(listener);
             for(KivaAGV agv:AGVManager.getInstance().getAgvList()) {
                 agv.removeAGVDataListener(listener);
             }
         }
     }

    /**
     * @return the agvDataListenerList
     */
    public List<AGVDataListener> getAgvDataListenerList() {
        return agvDataListenerList;
    }
      
}
