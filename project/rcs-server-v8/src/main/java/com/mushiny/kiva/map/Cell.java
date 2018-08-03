/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.kiva.path.astart.Node;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.server.AGV;
import com.mushiny.rcs.server.KivaAGV;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 * 格子的抽象表示
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface Cell {
    //获得地址码ID
    public long getAddressCodeID();

    //赋予地址码ID
    public void setAddressCode(long addressCodeID);

    //区域
    public void setRectangle(Rectangle R);

    public Rectangle getRectangle();

    public void setCellViewRectangle(Rectangle R);

    public Rectangle getCellViewRectangle();

    public void setCellColor(int color);

    public int getCellColor();

    public void setCellMarginColor(int color);

    public int getCellMarginColor();

    public int getCellPreviousColor();

    public void setCellPreviousColor(int color);

    public int getCellPreviousMarginColor();

    public void setCellPreviousMarginColor(int color);

    public void setRobotAction(RobotAction robotAction);

    public RobotAction getRobotAction();
    
    public void setRobotAction(SeriesPath sp,RobotAction robotAction);
    
    public void removeRobotAction(SeriesPath sp);
    
    public LinkedList<RobotAction> getRobotActionList(SeriesPath sp);
    
  
    
   //-- public void setLocked(boolean locked);
    
   
    
    public void setLocked(KivaAGV agv);
    
    public void setUnLocked();
    
    public boolean isLocked();
    
    public boolean isLocked_MapLock();
    
     public boolean setUnLocked_MapLock(SeriesPath sp);
    
    public boolean checkAndLocked_MapLock(KivaAGV agv);//具有优先级功能的锁格
    
    public boolean isSelected() ;
    
    public void setSelected(boolean selected);

    public byte[] toPathActionBytes(SeriesPath sp);

    boolean isDownNode(Node node);
}
