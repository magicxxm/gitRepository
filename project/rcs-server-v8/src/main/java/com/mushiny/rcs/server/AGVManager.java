/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.listener.AGVDataListener;
import com.mushiny.rcs.listener.AGVTimeoutListener;
import com.mushiny.rcs.listener.AGVListener;
import com.mushiny.rcs.listener.RCSListenerManager;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 维护一组连接到服务器的AGV
 *
 * @author aricochen
 */
public class AGVManager {

    private static Logger LOG = LoggerFactory.getLogger(AGVManager.class.getName());
    private static AGVManager instance;
    private static AGVTimeoutManager timeoutInstane;
    private final List<KivaAGV> agvList = new CopyOnWriteArrayList();
    private final List<AGVListener> agvListenerList = new CopyOnWriteArrayList();

    private AGVManager() {
    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new AGVManager();
            timeoutInstane = AGVTimeoutManager.getInstance();
        }
    }
    public static AGVManager getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }

    /*public static AGVManager getInstance() {
        if (instance == null) {
            instance = new AGVManager();
            timeoutInstane = AGVTimeoutManager.getInstance();
        }
        return instance;
    }*/

    public synchronized void notifyAGVOpenConnection(KivaAGV agv) {
        if (!agvList.contains(agv)) {
            getAgvList().add(agv);
        }
        for (AGVListener listener : agvListenerList) {
            agv.registeAGVListener(listener);
            listener.OnAGVOpenConnection2RCS(agv);
        }
        for(AGVDataListener listener:RCSListenerManager.getInstance().getAgvDataListenerList()) {
            agv.registeAGVDataListener(listener);
        }
    }
      public synchronized void notifyAGVCloseConnection(KivaAGV agv) {
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVCloseConnection2RCS(agv);
        }
    }

    /*
     此方法不用在TCP连接断开后,用在重新连接合并后
     */
    public synchronized void removeAGVFromAGVList(KivaAGV robot) {
        if (getAgvList().remove(robot)) {
            robot = null;
        }
    }

    public synchronized KivaAGV getAGVBySession(IoSession session) {
        for (KivaAGV robot : getAgvList()) {
            if (robot.getSession().equals(session)) {
                return robot;
            }
        }
        return null;
    }

    public synchronized KivaAGV getFirstRobot() {
        if (getAgvList().isEmpty()) {
            return null;
        }

        return getAgvList().get(0);
    }

    public synchronized KivaAGV getAGVByID(long agvID) {
        for (KivaAGV agv : agvList) {
            if (agv.getID() == agvID) {
                return agv;
            }
        }
        return null;
    }

    public synchronized int getAGVCount() {
        return getAgvList().size();
    }

    /**
     * @return the robotList
     */
    public synchronized List<KivaAGV> getAgvList() {
        return agvList;
    }

    public synchronized void registeAGVListener(AGVListener listener) {
        if (listener == null) {
            return;
        }
        if (!agvListenerList.contains(listener)) {
            agvListenerList.add(listener);
        }
        for (KivaAGV agv : getAgvList()) {
            agv.registeAGVListener(listener);
        }
    }
     public synchronized void removeAGVListener(AGVListener listener) {
        if (listener == null) {
            return;
        }
        if (agvListenerList.contains(listener)) {
            agvListenerList.remove(listener);
        }
        for (KivaAGV agv : getAgvList()) {
            agv.removeAGVListener(listener);
        }
    }

    public synchronized void registeAGVTimeoutListener(AGVTimeoutListener timeoutListener) {
        if (timeoutInstane != null) {
            timeoutInstane.registeAGVTimeoutListener(timeoutListener);
        }
    }

    public synchronized void removeAGVTimeoutListener(AGVTimeoutListener timeoutListener) {
        if (timeoutListener != null) {
            timeoutInstane.removeAGVTimeoutListener(timeoutListener);
        }
    }

    /*
      所有AGV停到最近二维码
     */
    public synchronized void allAGVStopNearCode() {
        for (KivaAGV agv : getAgvList()) {
            agv.stopNearCodeCommand();
        }
    }

    /*
     所有AGV从状态"停到最近二维码"到"启动"
     */
    public synchronized void allAGVStartFromStopNearCode() {
        for (KivaAGV agv : getAgvList()) {
            agv.startCommand();
        }
    }
    /*
     把需要人工处理的AGV从RCS队列中移除,
     AGV需要断开RCS后,再调用此方法
    */
    public synchronized void removeAGVFromRCS(long agvID) {
        KivaAGV tmpAGV=null;
        for (KivaAGV agv : getAgvList()) {
           if(agv.getID()==agvID) {
               tmpAGV = agv;
               break;
           }
        }
        if(tmpAGV==null) {
            return;
        }
        if(tmpAGV.getAGVStatus()==AGVConfig.AGV_STATUS_NO_CONNECTION) {
            agvList.remove(tmpAGV);
        }
    }

}
