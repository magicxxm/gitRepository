package com.mingchun.mu.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.server.KivaAGV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Laptop-6 on 2017/9/29.
 */
public class TriangleRotateAreaNew {

    private Logger LOG = LoggerFactory.getLogger(TriangleRotateAreaNew.class.getName());

    private long workstationID;// 工作站
    private CellNode enterCellNode; // 入口点
    private CellNode rotationExitCellNode; // 旋转并出口点


    public TriangleRotateAreaNew(long workstationID, CellNode enterCellNode, CellNode rotationExitCellNode) {
        this.workstationID = workstationID;
        this.enterCellNode = enterCellNode;
        this.rotationExitCellNode = rotationExitCellNode;
    }

    public void lockRotateArea(KivaAGV agv) {
        enterCellNode.setLocked(agv);
        rotationExitCellNode.setLocked(agv);
        LOG.info("旋转区锁定[enterCellNode="+enterCellNode.getAddressCodeID()+", rotationExitCellNode="+rotationExitCellNode.getAddressCodeID()+"]");
    }


    public void unlockRotationArea(){
        enterCellNode.setUnLocked();
        rotationExitCellNode.setUnLocked();
        LOG.info("旋转区解锁[enterCellNode="+enterCellNode.getAddressCodeID()+", rotationExitCellNode="+rotationExitCellNode.getAddressCodeID()+"]");
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
    public boolean isRotationExitCellNode(CellNode cellNode){
        if(cellNode == null){
            return false;
        }
        if(cellNode.equals(rotationExitCellNode)){
            return true;
        }
        return false;
    }

    public CellNode getEnterCellNode() {
        return enterCellNode;
    }
    public void setEnterCellNode(CellNode enterCellNode) {
        this.enterCellNode = enterCellNode;
    }
    public long getWorkstationID() {
        return workstationID;
    }
    public void setWorkstationID(long workstationID) {
        this.workstationID = workstationID;
    }
    public CellNode getRotationExitCellNode() {
        return rotationExitCellNode;
    }
    public void setRotationExitCellNode(CellNode rotationExitCellNode) {
        this.rotationExitCellNode = rotationExitCellNode;
    }
}
