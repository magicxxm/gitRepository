package com.mingchun.mu.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.server.KivaAGV;

import java.net.PortUnreachableException;

/**
 * Created by Laptop-6 on 2017/9/29.
 */
public class TriangleRotateArea {

    private long workstationID;// 工作站
    private CellNode enterCellNode; // 入口点
    private CellNode rotationCellNode; // 旋转点
    private CellNode exitCellNode; // 出口点


    public TriangleRotateArea(long workstationID, CellNode enterCellNode, CellNode rotationCellNode, CellNode exitCellNode) {
        this.workstationID = workstationID;
        this.enterCellNode = enterCellNode;
        this.rotationCellNode = rotationCellNode;
        this.exitCellNode = exitCellNode;
    }

    public void lockRotateArea(KivaAGV agv) {
        enterCellNode.setLocked(agv);
        rotationCellNode.setLocked(agv);
        exitCellNode.setLocked(agv);
    }


    public void unlockRotationArea(){
        enterCellNode.setUnLocked();
        rotationCellNode.setUnLocked();
        exitCellNode.setUnLocked();
    }



    public CellNode getEnterCellNode() {
        return enterCellNode;
    }
    public void setEnterCellNode(CellNode enterCellNode) {
        this.enterCellNode = enterCellNode;
    }
    public CellNode getRotationCellNode() {
        return rotationCellNode;
    }
    public void setRotationCellNode(CellNode rotationCellNode) {
        this.rotationCellNode = rotationCellNode;
    }
    public CellNode getExitCellNode() {
        return exitCellNode;
    }
    public void setExitCellNode(CellNode exitCellNode) {
        this.exitCellNode = exitCellNode;
    }
    public boolean isEnterCellNode(CellNode cellNode){
        if(cellNode == null){
            return false;
        }
        if(cellNode.equals(enterCellNode)){
            return true;
        }
        return false;
    }
    public boolean isExitCellNode(CellNode cellNode){
        if(cellNode == null){
            return false;
        }
        if(cellNode.equals(exitCellNode)){
            return true;
        }
        return false;
    }
}
