/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.listener;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.server.AGVMessage;
import java.util.LinkedList;

/**
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface AGVListener {
    //AGV发送全局路径
    public void OnSendGlobalPath2AGV(AGVMessage agv,SeriesPath globalPath);
    //AGV解锁CELL
    public void OnAGVUnLockedCell(AGVMessage agv,LinkedList<CellNode> unLockedCellNodeList);
    //AGV锁定CELL
    public void OnAGVLockedCell(AGVMessage agv,LinkedList<CellNode> lockedCellNodeList);
    //AGV到达全局路径的目标CELL
    public void OnAGVArriveAtGlobalPathTargetCell(AGVMessage agv,SeriesPath globalPath);
   
    //AGV状态事件
    public void OnAGVStatusChange(AGVMessage agv,int oldStatus,int newStatus);
    //AGV位置改变
    public void OnAGVPositionChange(AGVMessage agv,long oldAddressIDCode,long newAddressIDCode);
    //AGV连接到RCS
    public void  OnAGVOpenConnection2RCS(AGVMessage agv);
    //AGV关闭到RCS
    public void OnAGVCloseConnection2RCS(AGVMessage agv);
    //AGV重新连接到RCS
    public void OnAGVRepeatConnection2RCS(AGVMessage agv);
    //AGV从新连接后地址码不在最后下发的范围之内异常,此时RCS终止所有此AGV路径缓存.
    public void OnAGVRepeatConnection2RCS_PositionError(AGVMessage agv);
    //路径中出现临时不可用的CELL
    public void OnAGVSPUnWalkedCell(AGVMessage agv,CellNode cellNode);
    //请求WCS重新发送路径
    public void OnAGVRequestWCSPath(AGVMessage agv);

    // mingchun.mu@mushiny.com  ---- 到达充电路径终点， 需要充电桩准备对接
    void onArrivedChargingPile(AGVMessage agv, long addressCodeID);
    
}
