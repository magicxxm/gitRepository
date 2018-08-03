/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.mingchun.mu.manager.KivaConfigToolModifyManager;
import com.mingchun.mu.mushiny.rcs.server.KivaAGVModifier;
import com.mushiny.kiva.path.SeriesPath;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * 具有存储AGV路径功能的容器
 *
 * @author 陈庆余 aricochen
 */
public class AGVSeriesPathContainer extends AGVProperty {

    private static Logger LOG = Logger.getLogger(AGVSeriesPathContainer.class.getName());

    protected final PropertyChangeSupport changeSupport;

   //-- protected final LinkedList<SeriesPath> seriesPathLinkedList = new LinkedList();
    protected final List<SeriesPath> seriesPathLinkedList = new CopyOnWriteArrayList();
    protected SeriesPath tempNextGlobalSeriesPath = null;


    protected KivaConfigToolModifyManager kivaConfigToolModifyManager = KivaConfigToolModifyManager.getInstance();


    public AGVSeriesPathContainer() {
        super();
        changeSupport = new PropertyChangeSupport(this);
    }

    public synchronized void putGlobalSeriesPath(SeriesPath seriesPaht) {
        //--seriesPathLinkedList.addLast(seriesPaht);
         seriesPathLinkedList.add(seriesPaht);
    }

    public synchronized SeriesPath getNextGlobalSeriesPath() {
        if (seriesPathLinkedList.size() > 0) {
            // --return seriesPathLinkedList.removeFirst();
            setRotationFinished(false); // 获取下一条路径前将之前所有完成动作置为false，以便获取起点的转弯完成动作
            setPathResponse(-1); // 获取下一条路径前将以前的正常应答去掉，以便能够下发转弯起始点
            setFirstNodeSendTime(0); // 获取下一条路径前第一次等待的5s去掉，以便能够下发转弯起始点
            return seriesPathLinkedList.remove(0);
        } else {
            return null;
        }
    }

    public List<SeriesPath> getSeriesPathLinkedList() {
        return seriesPathLinkedList;
    }

    public void registerPropertyChange(PropertyChangeListener listener) {
        if (listener != null) {
            changeSupport.addPropertyChangeListener(listener);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    public SeriesPath getTempNextGlobalSeriesPath() {
        return tempNextGlobalSeriesPath;
    }

    public void setTempNextGlobalSeriesPath(SeriesPath tempNextGlobalSeriesPath) {
        this.tempNextGlobalSeriesPath = tempNextGlobalSeriesPath;
    }
}
