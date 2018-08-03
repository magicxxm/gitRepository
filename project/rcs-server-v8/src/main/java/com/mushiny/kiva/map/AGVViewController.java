/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import com.mushiny.rcs.listener.CellListener;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.server.KivaAGV;
import java.awt.Toolkit;
import com.mushiny.rcs.listener.AGV2CellListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVViewController extends MapViewCommonOperation implements AGV2CellListener, CellListener {

    private static Logger LOG = LoggerFactory.getLogger(AGVViewController.class.getName());
    private static AGVViewController instance;

    private AGVViewController() {
    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new AGVViewController();
        }
    }

    public static AGVViewController getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }
/*
    public static AGVViewController getInstance() {
        if (instance == null) {
            instance = new AGVViewController();
        }
        return instance;
    }
*/

    public void installMapWindow(MapWindow mapWindow) {
        super.installMapWindow(mapWindow);
    }

    public KivaMap getKivaMap() {
        return getMapWindow().getMap();
    }

    @Override
    public void OnAGVEnterMapCell(KivaAGV agv,CellNode enterCellNode) {
        updateCellNodeView(enterCellNode);
        Toolkit.getDefaultToolkit().beep();
    }

    @Override
    public void OnAGVLeaveMapCell(KivaAGV agv,CellNode leaveCell) {
        updateCellNodeView(leaveCell);
    }

    
    @Override
     public void OnCellWalkable(CellNode cellNode){
         updateCellNodeView(cellNode);
     }
    @Override
     public void OnCellUnWalkable(CellNode cellNode){
         updateCellNodeView(cellNode);
     }
    @Override
     public void OnCellUnLocked(CellNode unLockedCellNode){
         updateCellNodeView(unLockedCellNode);
     }
    @Override
     public void OnCellLocked(CellNode lockedCellNode){
         updateCellNodeView(lockedCellNode);
     }
    @Override
     public void OnCellInSeriesPath(CellNode cellNode,SeriesPath globalSeriesPath){
         updateCellNodeView(cellNode);
     }
    @Override
    public void  OnCellNoInSeriesPath(CellNode cellNode,SeriesPath globalSeriesPath){
         updateCellNodeView(cellNode);
    }
    
    @Override
     public void OnCellCommonUpdate(CellNode cellNode){
         updateCellNodeView(cellNode);
     }
}
