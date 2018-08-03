/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.map;

import com.mushiny.kiva.path.RotateArea;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.kiva.path.astart.Node;
import com.mushiny.rcs.server.AGV;
import com.mushiny.rcs.server.KivaAGV;
import java.util.LinkedList;

/**
 * CELL和Node公用接口
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public interface CellNode extends Cell, Node {

    public boolean isAGVIn();

    public void setAGVIn(KivaAGV agv);

    public void setAGVOut(KivaAGV agv);

    public KivaAGV getAGV();

    public void setInRotateArea(RotateArea ra);

    public RotateArea isInRotateArea();

    public void setRotateAreaEnter();

    public boolean isRotateAreaEnter();

    public void setRotateAreaExit();

    public boolean isRotateAreaExit();

    public boolean isInGlobalPath();

    public void setInGlobalPath(SeriesPath globalPath);

    public void setNoInGlobalPath(SeriesPath globalPath);

    public LinkedList<SeriesPath> getSeriesPath();

    // public CellNode cellNodeClone();
    public void setFollowCellNode(boolean flag);//设置为跟车点

    public boolean isFollowCellNode();

    KivaAGV getNowLockedAGV();



    // 是否不用于计算锁格超时（直接初始化 安装）
    boolean isUnlockedTimeout();
    void setUnlockedTimeout(boolean unlockedTimeout);

    // 是否是临时不可走点
    boolean isTempUnwalkable();
    void setTempUnwalkable(boolean tempUnwalkable);

    boolean isChangingCost();
    void setChangingCost(boolean changingCost);

    int getUpDistance();
    void setUpDistance(int upDistance);
    int getDownDistance();
    void setDownDistance(int downDistance);
    int getLeftDistance();
    void setLeftDistance(int leftDistance);
    int getRightDistance();
    void setRightDistance(int rightDistance);


}
