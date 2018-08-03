/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.pathviewtool;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.listener.AGVListener;
import com.mushiny.rcs.listener.AGVTimeoutListener;
import com.mushiny.rcs.listener.CellListener;
import com.mushiny.rcs.listener.RCSListenerManager;
import com.mushiny.rcs.server.AGVMessage;
import com.mushiny.rcs.server.KivaAGV;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVListView extends PathView implements AGVListener, AGVTimeoutListener, CellListener {

    private List<AGVEventContent> eventList = new CopyOnWriteArrayList();
    private int showEventCount = 20;
    private int heightSetup = 5;
    private float DEFAULT_POSITION_X = 10;
    private float DEFAULT_POSITION_Y = 10;
    private float bPositionX = DEFAULT_POSITION_X;
    private float bPositionY = DEFAULT_POSITION_Y;
    private Color backgroudColor = Color.BLACK;

    public AGVListView() {
        RCSListenerManager.getInstance().registeAGVListener(this);
        RCSListenerManager.getInstance().registeAGVTimeoutListener(this);
        RCSListenerManager.getInstance().registeCellListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //g2.setColor(backgroudColor);
        //g2.fill(getBounds());
        showAGVEvent(g2);
    }

    //AGV发送全局路径
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
//        AGVEventContent eventContent = new AGVEventContent("到达锁格目标:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort() + ",targetAddressCodeID=" + targetCellNode.getAddressCodeID());
//        eventList.add(eventContent);
//        updateView();
    }

    //AGV状态事件
    public void OnAGVStatusChange(AGVMessage agv, int oldStatus, int newStatus) {
        AGVEventContent eventContent = new AGVEventContent("AGV状态:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort() + ",status=" + AGVConfig.getAGVStatusInfo(newStatus));
        eventList.add(eventContent);
        updateView();
    }

    //AGV位置改变
    public void OnAGVPositionChange(AGVMessage agv, long oldAddressIDCode, long newAddressIDCode) {
//        AGVEventContent eventContent = new AGVEventContent("新位置:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort() + ",addressCodeID=" + agv.getCurrentAddressCodeID());
//        eventList.add(eventContent);
//        updateView();
    }

    //AGV连接到RCS
    public void OnAGVOpenConnection2RCS(AGVMessage agv) {
        AGVEventContent eventContent = new AGVEventContent("新连接:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort());
        eventList.add(eventContent);
        updateView();
    }

    //AGV关闭到RCS
    public void OnAGVCloseConnection2RCS(AGVMessage agv) {
        AGVEventContent eventContent = new AGVEventContent("关闭连接:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort());
        eventList.add(eventContent);
        updateView();
    }

    //AGV重新连接到RCS
    public void OnAGVRepeatConnection2RCS(AGVMessage agv) {
        AGVEventContent eventContent = new AGVEventContent("重连接:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort());
        eventList.add(eventContent);
        updateView();
    }

    /*
    AGV从新连接后地址码不在最后下发的范围之内异常,此时RCS终止所有此AGV路径缓存.
     */
    public void OnAGVRepeatConnection2RCS_PositionError(AGVMessage agv) {
        AGVEventContent eventContent = new AGVEventContent("重连接:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort() + ",新位置错误(需人工处理AGV),地址码=" + agv.getCurrentAddressCodeID());
        eventList.add(eventContent);
        updateView();
    }
    //路径中出现临时不可用的CELL

    public void OnAGVSPUnWalkedCell(AGVMessage agv, CellNode cellNode) {

    }

    //请求WCS重新发送路径
    public void OnAGVRequestWCSPath(AGVMessage agv) {
        AGVEventContent eventContent = new AGVEventContent("请求WCS重新规划路径:AGV_ID=" + agv.getID() );
        eventList.add(eventContent);
        updateView();
    }

    @Override
    public void onArrivedChargingPile(AGVMessage agv, long addressCodeID) {

    }

    //心跳超时
    public void onAGVBeatTimeout(KivaAGV agv) {

    }

    //实时数据包超时
    public void onAGVRTTimeout(KivaAGV agv) {

    }

    //心跳或实时数据包超时
    public void onAGVBeatOrRTTimeout(KivaAGV agv) {
        AGVEventContent eventContent = new AGVEventContent("心跳或实时数据超时:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort());
        eventList.add(eventContent);
        updateView();
    }

    //任务超时
    public void onAGVPositionNoChanageTimeout(KivaAGV agv, Map<String, Object> paramMap) {
        AGVEventContent eventContent = new AGVEventContent("AGV赋予任务,任务超时:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort());
        eventList.add(eventContent);
        updateView();
    }
    //锁格超时

    public void onAGVLockCellTimeout(KivaAGV agv, Map<String, Object> paramMap) {
        AGVEventContent eventContent = new AGVEventContent("AGV赋予任务,锁超时:AGV_ID=" + agv.getID() + ",IP=" + agv.getIP() + ",PORT=" + agv.getPort());
        eventList.add(eventContent);
        updateView();
    }

    public void OnCellWalkable(CellNode cellNode) {

    }

    public void OnCellUnWalkable(CellNode cellNode) {

    }

    public void OnCellUnLocked(CellNode unLockedCellNode) {
//        AGVEventContent eventContent = new AGVEventContent("CELL UNLOCKED:AddCodeID=" + unLockedCellNode.getAddressCodeID());
//        eventList.add(eventContent);
//        updateView();
    }

    public void OnCellLocked(CellNode lockedCellNode) {
//        AGVEventContent eventContent = new AGVEventContent("CELL LOCKED:AddCodeID=" + lockedCellNode.getAddressCodeID());
//        eventList.add(eventContent);
//        updateView();
    }

    public void OnCellInSeriesPath(CellNode cellNode, SeriesPath globalSeriesPath) {

    }

    public void OnCellNoInSeriesPath(CellNode cellNode, SeriesPath globalSeriesPath) {

    }

    public void OnCellCommonUpdate(CellNode cellNode) { //一般的普通信息更新时调用

    }

    public void showAGVEvent(Graphics2D g2) {
        bPositionX = DEFAULT_POSITION_X;
        bPositionY = DEFAULT_POSITION_Y;
        if (eventList.size() > showEventCount) {
            int tmpCount = eventList.size() - showEventCount;
            for (int i = 0; i < tmpCount; i++) {
                eventList.remove(0);
            }
        }
        for (AGVEventContent content : eventList) {
            String tmp = content.getEventContent();
            float acent = getFm().getLineMetrics(tmp, g2).getAscent();
            bPositionY += acent;
            g2.drawString(tmp, bPositionX, bPositionY);
            float decent = getFm().getLineMetrics(tmp, g2).getDescent();
            bPositionY += decent + heightSetup;
        }
    }
}
