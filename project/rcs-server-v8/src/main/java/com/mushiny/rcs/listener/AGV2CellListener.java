/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.listener;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.server.KivaAGV;

/**
 *AGV进出CELL监听
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface AGV2CellListener {
    public void OnAGVLeaveMapCell(KivaAGV agv,CellNode leaveCell);
    public void OnAGVEnterMapCell(KivaAGV agv,CellNode enterCellNode);
}
