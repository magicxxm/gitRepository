/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path;

import com.mushiny.rcs.kiva.bus.action.RobotAction;
import java.util.LinkedList;

/**
 *SeriesPath 与 Action映射
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class CellNodeSeriesPathRobotAction {
    private SeriesPath seriesPath;
    private LinkedList<RobotAction> robotActionList = new LinkedList();
    
    public CellNodeSeriesPathRobotAction(SeriesPath sp,RobotAction action) {
        this.seriesPath = sp;
        robotActionList.addLast(action);
    }
    
    public SeriesPath getSeriesPath() {
        return seriesPath;
    }
    
    public LinkedList<RobotAction> getRobotActions() {
        return robotActionList;
    }
    public void addRobotAction(RobotAction action) {
        robotActionList.addLast(action);
    }
}
