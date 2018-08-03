/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import com.mushiny.rcs.listener.MapViewChangedListener;
import com.aricojf.platform.mina.message.robot.RobotRTMessage;
import com.mushiny.kiva.path.RotateArea;
import com.mushiny.kiva.path.RotateAreaManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.listener.AGVListener;
import com.mushiny.rcs.listener.CellListener;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.AGVMessage;
import com.mushiny.rcs.server.KivaAGV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.text.html.parser.Entity;
import javax.tools.JavaCompiler;

/**
 * RCS系统地图
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class KivaMap extends Map implements MapViewChangedListener, AGVListener {

    private static final Logger LOG = LoggerFactory.getLogger(KivaMap.class.getName());
    private final RotateAreaManager rotateAreaManager;
    private final List<CellNode> tmpUnWalkedCellNodeList = new CopyOnWriteArrayList();
    private CellNode tmpCellNode;

    public KivaMap(int row, int col) {
        super(row, col);
        rotateAreaManager = RotateAreaManager.getInstance();
    }

    /**
     *  mingchun.mu@mushiny.com   安装不用锁格超时计算的点 
     * @param unlockedCellTimeoutCellNodes
     * @return
     */
    @Override
    public boolean installUnlockedCellTimeoutCellNodes(LinkedList<Long> unlockedCellTimeoutCellNodes) {
        if(unlockedCellTimeoutCellNodes == null){
            LOG.error("安装不用于锁格超时计算的点集 为null ！！！");
            return false;
        }
        if(unlockedCellTimeoutCellNodes.size() == 0){
            LOG.error("安装不用于锁格超时计算的点集 没有元素！！！");
            return false;
        }
        for (long v : unlockedCellTimeoutCellNodes) {
            CellNode cellNode = getMapCellByAddressCodeID(v);
            if (cellNode == null) {
                LOG.error("安装不用于锁格超时计算的点集中  有不存在于地图中的地址码！！！");
                return false;
            }
        }
        for (long v : unlockedCellTimeoutCellNodes) {
            CellNode cellNode = getMapCellByAddressCodeID(v);
            cellNode.setUnlockedTimeout(true);
        }
        return true;
    }

    /**
     * mingchun.mu@mushiny.com
     *
     * 手动 解锁 锁定格子
     * @param addressCodeID 解锁指定的地址码
     */
    public synchronized void unlockCell(long addressCodeID){
        if(addressCodeID <= 0){
            LOG.error("解锁地址码必须（"+addressCodeID+"）大于0");
            return;
        }
        if(addressCodeID > MapManager.getInstance().getMap().getMaxAddressCodeID()){
            LOG.error("解锁地址码（"+addressCodeID+"）必须小于地图最大地址码（"+addressCodeID+"）");
            return;
        }
        CellNode willUnlockCell = getMapCellByAddressCodeID(addressCodeID);
        if(unWalkedCellNodeList.contains(willUnlockCell)){
            LOG.error("解锁地址码（"+addressCodeID+"）在不可走区域！！！");
            return;
        }
        willUnlockCell.setUnLocked();
        LOG.info("人工解锁格子("+willUnlockCell.getAddressCodeID()+")");
    }
    /**
     * mingchun.mu@mushiny.com
     *
     * 手动 解锁 锁定格子
     * @param addressCodeIDList 解锁指定的地址码
     */
    public synchronized void unlockCell(List<Long> addressCodeIDList){
        if(addressCodeIDList == null){
            LOG.error("解锁地址码集合为空！！！");
            return;
        }
        if(addressCodeIDList.size() == 0){
            LOG.error("解锁地址码集合无元素！！！");
            return;
        }
        for(Long L: addressCodeIDList){
            unlockCell(L);
        }
    }
    /**
     * mingchun.mu@mushiny.com
     *
     * 手动 锁定 格子
     * @param addressCodeID 解锁指定的地址码
     */
    public synchronized void lockCell(long addressCodeID){
        if(addressCodeID <= 0){
            LOG.error("要锁地址码必须（"+addressCodeID+"）大于0");
            return;
        }
        if(addressCodeID > MapManager.getInstance().getMap().getMaxAddressCodeID()){
            LOG.error("要锁地址码（"+addressCodeID+"）必须小于地图最大地址码（"+addressCodeID+"）");
            return;
        }
        CellNode willLockCell = getMapCellByAddressCodeID(addressCodeID);
        if(unWalkedCellNodeList.contains(willLockCell)){
            LOG.error("要锁地址码（"+addressCodeID+"）在不可走区域！！！");
            return;
        }
        willLockCell.setLocked(null);
        LOG.info("人工锁定格子("+willLockCell.getAddressCodeID()+")");
    }
    /**
     * mingchun.mu@mushiny.com
     *
     * 手动 锁定 格子
     * @param addressCodeIDList 解锁指定的地址码集合
     */
    public synchronized void lockCell(List<Long> addressCodeIDList){
        if(addressCodeIDList == null){
            LOG.error("要锁地址码集合为空！！！");
            return;
        }
        if(addressCodeIDList.size() == 0){
            LOG.error("要锁地址码集合无元素！！！");
            return;
        }
        for(Long L: addressCodeIDList){
            lockCell(L);
        }
    }




    public void initKivaMap() {
        initMap();
        createNodeRelation();
        AGVManager.getInstance().registeAGVListener(this);
    }

    /*
     安装不可walked点
     */
    public boolean installNoWalkedList(LinkedList<Long> nowWalkedAddressCodeIDList) {
        for (long v : nowWalkedAddressCodeIDList) {
            CellNode cellNode = getMapCellByAddressCodeID(v);
            if (cellNode == null) {
                LOG.error("####安装不可walked地址出错，地址码不合法!");
                return false;
            }
        }
        for (long v : nowWalkedAddressCodeIDList) {
            CellNode cellNode = getMapCellByAddressCodeID(v);
            cellNode.setWalkable(false);
        }
        return true;
    }

    /*
     安装旋转区域
     */
    public boolean installRotateArea(LinkedList<RotateArea> rotateAreaList) {
        for (RotateArea rotateArea : rotateAreaList) {
            if (!rotateAreaManager.addRotateArea(rotateArea)) {
                LOG.error("####安装旋转区出错,旋转区不合法!");
                return false;
            }
        }
        return true;
    }

    /*
     卸载旋转区域
     */
    public boolean unInstallRotateArea(RotateArea ra) {
        rotateAreaManager.removeRotateArea(ra);
        return true;
    }

    /*
     安装临时不可走的CELL
     */
    public void installTempUnWalkedCellNode(long addressCodeID) {
        tmpCellNode = getMapCellByAddressCodeID(addressCodeID);
        if (tmpCellNode != null) {
            if (!tmpUnWalkedCellNodeList.contains(tmpCellNode)) {
                getTmpUnWalkedCellNodeList().add(tmpCellNode);
            }
        }
    }

    /*
     卸载临时不可的Cell
     */
    public void unInstallTempUnWalkedCellNode(long addressCodeID) {
        tmpCellNode = getMapCellByAddressCodeID(addressCodeID);
        if (tmpCellNode != null) {
            getTmpUnWalkedCellNodeList().remove(tmpCellNode);
        }
    }
    /*
     安装跟车直行点
    */
    public void  installFollowCellNode(LinkedList<Long> followCellNodeAddressCodeIDList) {
        for (long v : followCellNodeAddressCodeIDList) {
            CellNode cellNode = getMapCellByAddressCodeID(v);
            LOG.info("安装跟车直行点 地址码（"+v+"）");
            if (cellNode == null) {
                LOG.error("####安装跟车直行CELL出错，地址码("+v+")不合法!");
            }else{
                cellNode.setFollowCellNode(true);
            }
        }
    }
     /*
     卸载跟车直行点
    */
    public void  unInstallFollowCellNode(LinkedList<Long> followCellNodeAddressCodeIDList) {
        for (long v : followCellNodeAddressCodeIDList) {
            CellNode cellNode = getMapCellByAddressCodeID(v);
            if (cellNode == null) {
                LOG.error("####卸载跟车直行CELL出错，地址码不合法!");
            }
        }
        for (long v : followCellNodeAddressCodeIDList) {
            CellNode cellNode = getMapCellByAddressCodeID(v);
            cellNode.setFollowCellNode(false);
        }
    }

    /**
     *
     * 更改cost值的点
     */
    public void changingCellsCost(List<CellNode> cellNodeList, boolean isChangingCost){
        if(cellNodeList == null
                || cellNodeList.size() == 0){
            LOG.error("需要更改cost的点集为空！！！");
            return;
        }
        LOG.info("更改了cost值的点集cellNodeList="+cellNodeList);
        for(CellNode cellNode : cellNodeList){
            cellNode.setChangingCost(isChangingCost);
        }
    }


//============================CellNode监听======================================

    public void registeCellListener(CellListener listener) {
        for (int row = 0; row < getMapRow(); row++) {
            for (int col = 0; col < getMapCol(); col++) {
                mapNodes[row][col].registeCellListener(listener);
            }
        }
    }

    public void removeCellListener(CellListener listener) {
        for (int row = 0; row < getMapRow(); row++) {
            for (int col = 0; col < getMapCol(); col++) {
                mapNodes[row][col].removeCellListener(listener);
            }
        }
    }

//==============================================================================
    @Override
    public void onLoadingMapData() {

    }

    @Override
    public void onZoom(AffineTransform transform) {
        this.transform = transform;
    }

    @Override
    public void onFlip(int x, int y, double theta) {
        changeCellViewRectangleByFlipTheta(x, y, theta);
    }
//==============================================================================

    @Override
    public void onReceivedAGVRTMessage(RobotRTMessage data) {
        super.onReceivedAGVRTMessage(data);
    }

//    /*
//     检查cellNode串，全部可锁并且锁住返回true,不可锁返回false
//     */
//    public boolean checkCellListAndLocked(LinkedList<CellNode> cellNodeList) {
//        synchronized (MapLock.LOCK) {
//            for (CellNode cellNode : cellNodeList) {
//                if (cellNode.isLocked()) {
//                    return false;
//                }
//            }
//            for (CellNode cellNode : cellNodeList) {
//                cellNode.setLocked();
//            }
//            return true;
//        }
//    }
    //AGV收到全局路径
    public void OnSendGlobalPath2AGV(AGVMessage agv, SeriesPath globalPath) {

    }

    //AGV解锁CELL
    public void OnAGVUnLockedCell(AGVMessage agv, LinkedList<CellNode> unLockedCellNodeList) {

    }

    //AGV锁定CELL
    public void OnAGVLockedCell(AGVMessage agv, LinkedList<CellNode> lockedCellNodeList) {

    }

    //AGV到达全局路径的目标CELL
    public void OnAGVArriveAtGlobalPathTargetCell(AGVMessage agv, SeriesPath globalPath) {

    }

    //AGV到达锁格路径的目标CELL
    public void OnAGVArrivedAtLockPathTargetCell(AGVMessage agv, CellNode targetCellNode) {

    }

    //AGV状态事件
    public void OnAGVStatusChange(AGVMessage agv, int oldStatus, int newStatus) {

    }

    //AGV位置改变
    public void OnAGVPositionChange(AGVMessage agv, long oldAddressIDCode, long newAddressIDCode) {

        if(agv != null){
            LOG.info("AGV("+agv.getID()+")位置改变:oldAddressIDCode="+oldAddressIDCode+", newAddressIDCode="+newAddressIDCode);
        }else {
            LOG.info("AGV=null位置改变:oldAddressIDCode="+oldAddressIDCode+", newAddressIDCode="+newAddressIDCode);
        }

            MapCellNode leaveCellNode = getMapCellByAddressCodeID(oldAddressIDCode);
            MapCellNode newCellNode = getMapCellByAddressCodeID(newAddressIDCode);
            if (leaveCellNode != null) {
                leaveCellNode.setAGVOut((KivaAGV) agv);
                fireOnAGVLeaveMapCell((KivaAGV) agv, leaveCellNode);
            }
            if (newCellNode != null) {
                agv.setCurrentCellNode(newCellNode);
                newCellNode.setAGVIn((KivaAGV) agv);
                fireOnAGVEnterMapCell((KivaAGV) agv, newCellNode);
            }
    }

    //AGV连接到RCS
    public void OnAGVOpenConnection2RCS(AGVMessage agv) {

    }

    //AGV关闭到RCS
    public void OnAGVCloseConnection2RCS(AGVMessage agv) {

    }
    //AGV重新连接到RCS

    public void OnAGVRepeatConnection2RCS(AGVMessage agv) {

    }
    //AGV重新连接后有故障,不在锁格路径范围之内

    public void OnAGVRepeatConnection2RCS_PositionError(AGVMessage agv) {

    }
    //路径中出现临时不可用的CELL

    public void OnAGVSPUnWalkedCell(AGVMessage agv, CellNode cellNode) {

    }
    //请求WCS重新发送路径

    public void OnAGVRequestWCSPath(AGVMessage agv) {

    }

    @Override
    public void onArrivedChargingPile(AGVMessage agv, long addressCodeID) {

    }

    /**
     * @return the tmpUnWalkedCellNodeList
     */
    public List<CellNode> getTmpUnWalkedCellNodeList() {
        return tmpUnWalkedCellNodeList;
    }

}
