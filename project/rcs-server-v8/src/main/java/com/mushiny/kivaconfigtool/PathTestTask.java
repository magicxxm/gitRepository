/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kivaconfigtool;

import com.mushiny.rcs.server.AGV;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.listener.AGVListener;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.AGVMessage;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 *
 * @author aricochen
 */
public class PathTestTask extends Thread implements AGVListener {

    private static Logger LOG = LoggerFactory.getLogger(PathTestTask.class.getName());
    private final AGV agv;
    private CellNode startCellNode;
    private CellNode endCellNode;
    private WCSSeriesPath wcsPath;
    private WCSSeriesPath returnPath;
    //-- private final RCS2RobotPathMessage pathMessage;
    private boolean stop = false;
    private boolean loop = false;
    boolean isAGVBeginRun = false;

    public PathTestTask(AGV agv, WCSSeriesPath seriesPath, boolean loop) {
        this.agv = agv;
        this.wcsPath = seriesPath;
        this.loop = loop;
        seriesPath.setAgvID(agv.getID());
        wcsPath = seriesPath;
        //returnPath = wcsPath.getReturnSeriesPaths();
        startCellNode = wcsPath.getCellNodeList().getFirst();
        endCellNode = wcsPath.getCellNodeList().getLast();
    }

    @Override
    public void run() {
        AGVManager.getInstance().registeAGVListener(this);
        while (!isStop()) {
            if (!isAGVBeginRun) {
                prepareBegin();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }

    public void prepareBegin() {
        if (startCellNode.equals(wcsPath.getCellNodeList().getFirst())) {            
            //分拆路径
            //LinkedList<SeriesPath> canRunSeriesPathList = wcsPath.getRSSeriesPathList();
            //for (SeriesPath sp : canRunSeriesPathList) {
            LOG.info("=========路径测试程序，正常路径下发...");
                 LOG.debug("路径串测试,压栈路径串="+wcsPath);
                agv.putWCSSeriesPath(wcsPath);
            //}
            isAGVBeginRun = true;
            LOG.info("agv目前信息:"+agv);
            
        } else {
            if (startCellNode.equals(returnPath.getCellNodeList().getLast())) {
                //分拆路径
                LinkedList<SeriesPath> canRunSeriesPathList =  returnPath.getRSSeriesPathList();
                for (SeriesPath sp : canRunSeriesPathList) {
                    LOG.debug("路径串测试,压栈路径串="+sp);
                    agv.putGlobalSeriesPath(sp);
                }
                isAGVBeginRun = true;
                stop=true;///不用循环了....
                LOG.info("=========路径测试程序，掉头路径下发...");
            } else {
                LOG.warn("AGV目前没有位于路径的起点！！，等待AGV到达路径开始坐标....");
            }
        }
    }

    //AGV收到全局路径
    @Override
    public void OnSendGlobalPath2AGV(AGVMessage agv, SeriesPath globalPath) {

    }

    //AGV解锁CELL
    @Override
    public void OnAGVUnLockedCell(AGVMessage agv, LinkedList<CellNode> unLockedCellNodeList) {

    }

    //AGV锁定CELL
    public void OnAGVLockedCell(AGVMessage agv, LinkedList<CellNode> lockedCellNodeList) {

    }

    //AGV到达全局路径的目标CELL
    @Override
    public void OnAGVArriveAtGlobalPathTargetCell(AGVMessage agv, SeriesPath globalPath) {
        if (!isStop() && isLoop() && this.agv.equals(agv) && globalPath.getPathList().getLast().equals(endCellNode)) {
            LOG.debug("到达全路径终点，循环下发路径....");
            endCellNode = startCellNode;
            startCellNode = globalPath.getPathList().getLast();
            isAGVBeginRun = false;
        }
    }


    //AGV状态事件
    @Override
    public void OnAGVStatusChange(AGVMessage agv, int oldStatus, int newStatus) {

    }

    //AGV位置改变
    @Override
    public void OnAGVPositionChange(AGVMessage agv, long oldAddressIDCode, long newAddressIDCode) {
          
    }
    
    public void OnAGVBlocked(AGVMessage agvm,long addressCodeID) {
        
    }

    //AGV连接到RCS
    @Override
    public void OnAGVOpenConnection2RCS(AGVMessage agv) {

    }

    //AGV关闭到RCS
    @Override
    public void OnAGVCloseConnection2RCS(AGVMessage agv) {

    }
     //AGV重新连接到RCS
    public void OnAGVRepeatConnection2RCS(AGVMessage agv){
        
    }
    //AGV重新连接后有故障,不在锁格路径范围之内
     public void OnAGVRepeatConnection2RCS_PositionError(AGVMessage agv){
         
     }
      //路径中出现临时不可用的CELL
    public void OnAGVSPUnWalkedCell(AGVMessage agv,CellNode cellNode){
        
    }
       //请求WCS重新发送路径
    public void OnAGVRequestWCSPath(AGVMessage agv){
        
    }

    @Override
    public void onArrivedChargingPile(AGVMessage agv, long addressCodeID) {

    }

    /**
     * @return the brokenPath
     */
    public WCSSeriesPath getWcsPath() {
        return wcsPath;

    }

    /**
     * @param wcsPath the brokenPath to set
     */
    public void setWcsPath(WCSSeriesPath wcsPath) {
        this.wcsPath = wcsPath;
        returnPath = wcsPath.getReturnSeriesPaths();
        isAGVBeginRun = false;
    }

    /**
     * @return the loop
     */
    public boolean isLoop() {
        return loop;

    }

    /**
     * @param loop the loop to set
     */
    public void setLoop(boolean loop) {

        this.loop = loop;

    }

    /**
     * @return the stop
     */
    public boolean isStop() {
        return stop;

    }

    /**
     * @param stop the stop to set
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }

}
