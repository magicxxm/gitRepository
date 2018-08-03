/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.server.KivaAGV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 旋转区域
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RotateArea {
    private static Logger LOG = LoggerFactory.getLogger(RotateArea.class.getName());
    private long workstationID;
    private CellNode leftUpCellNode;
    private CellNode rightUpCellNode;
    private CellNode rightDownCellNode;
    private CellNode leftDownCellNode;
    private KivaAGV nowAGV;
   

    public RotateArea(long workstationID, CellNode leftUpCellNode, CellNode rightUpCellNode, CellNode rightDownCellNode, CellNode leftDownCellNode) {
        this.workstationID = workstationID;
        this.leftUpCellNode = leftUpCellNode;
        this.rightUpCellNode = rightUpCellNode;
        this.rightDownCellNode = rightDownCellNode;
        this.leftDownCellNode = leftDownCellNode;
    }

    /*
     检测旋转区域的合法性
     */
    public boolean isValidate() {
        if (getWorkstationID() == 0) {
            LOG.error("####旋转区域不合法(1.0)，旋转区域指定一个PODID");
            return false;
        }
        if (getLeftUpCellNode() == null || getRightUpCellNode() == null
                || getRightDownCellNode() == null || getLeftDownCellNode() == null) {
            LOG.error("####旋转区域不合法(1.1)，旋转区域必须是4个CELL");
            LOG.error(this.toString());
            return false;
        }
        if (!leftUpCellNode.isRightNode(rightUpCellNode)
                || !leftUpCellNode.isDownNode(leftDownCellNode)
                || !rightDownCellNode.isUpNode(rightUpCellNode)
                || !rightDownCellNode.isLeftNode(leftDownCellNode)) {
            LOG.error("####旋转区域不合法(1.2)，旋转区域必须是4个相邻的CELL");
            LOG.error(this.toString());
            return false;
        }

        return true;
    }

    /*
     判断两个rotateArea是否相等
     */
    public boolean equals(RotateArea rotateArea) {
        if (getWorkstationID() == rotateArea.getWorkstationID()
                && getLeftUpCellNode().equals(rotateArea.getLeftUpCellNode())
                && getRightUpCellNode().equals(rotateArea.getRightUpCellNode())
                && getRightDownCellNode().equals(rotateArea.getRightDownCellNode())
                && getLeftDownCellNode().equals(rotateArea.getLeftDownCellNode())) {
            return true;
        }

        return false;
    }

    /*
     判断两个rotateArea是否相交
     */
    public boolean isUion(RotateArea rotateArea) {
        if (getWorkstationID() == rotateArea.getWorkstationID()) {
            return true;
        }
        if (getLeftUpCellNode().equals(rotateArea.getLeftUpCellNode())
                || getLeftUpCellNode().equals(rotateArea.getRightUpCellNode())
                || getLeftUpCellNode().equals(rotateArea.getRightDownCellNode())
                || getLeftUpCellNode().equals(rotateArea.getLeftDownCellNode())) {
            return true;
        }
        if (getRightUpCellNode().equals(rotateArea.getLeftUpCellNode())
                || getRightUpCellNode().equals(rotateArea.getRightUpCellNode())
                || getRightUpCellNode().equals(rotateArea.getRightDownCellNode())
                || getRightUpCellNode().equals(rotateArea.getLeftDownCellNode())) {
            return true;
        }
        if (getRightDownCellNode().equals(rotateArea.getLeftUpCellNode())
                || getRightDownCellNode().equals(rotateArea.getRightUpCellNode())
                || getRightDownCellNode().equals(rotateArea.getRightDownCellNode())
                || getRightDownCellNode().equals(rotateArea.getLeftDownCellNode())) {
            return true;
        }
        if (getLeftDownCellNode().equals(rotateArea.getLeftUpCellNode())
                || getLeftDownCellNode().equals(rotateArea.getRightUpCellNode())
                || getLeftDownCellNode().equals(rotateArea.getRightDownCellNode())
                || getLeftDownCellNode().equals(rotateArea.getLeftDownCellNode())) {
            return true;
        }
        return false;
    }

    /*
     判断一个点是否在此旋转区域
     */
    public boolean isCellNodeInRotateArea(CellNode cellNode) {
        if (cellNode.equals(getLeftUpCellNode())
                || cellNode.equals(getRightUpCellNode())
                || cellNode.equals(getRightDownCellNode())
                || cellNode.equals(getLeftDownCellNode())) {
            return true;
        }
        return false;
    }

    /*
     判断一个点是否在此旋转区域
     */
    public boolean isCellNodeInRotateAreaByAddressCodeID(long addressCodeID) {
        if (addressCodeID == getLeftUpCellNode().getAddressCodeID()
                || addressCodeID == getRightUpCellNode().getAddressCodeID()
                || addressCodeID == getRightDownCellNode().getAddressCodeID()
                || addressCodeID == getLeftDownCellNode().getAddressCodeID()) {
            return true;
        }
        return false;
    }
    /*
     锁定此旋转区
    */
    public void setLocked(KivaAGV agv) {
        leftUpCellNode.setLocked(agv);
        rightUpCellNode.setLocked(agv);
        rightDownCellNode.setLocked(agv);
        leftDownCellNode.setLocked(agv);
        nowAGV = agv;
        LOG.info(nowAGV.getID()+"号车 锁定旋转区域：("+leftUpCellNode.getAddressCodeID()+","+rightUpCellNode.getAddressCodeID()+","+rightDownCellNode.getAddressCodeID()+","+leftDownCellNode.getAddressCodeID()+")");
    }
    public void setUnLocked() {
        leftUpCellNode.setUnLocked();
        rightUpCellNode.setUnLocked();
        rightDownCellNode.setUnLocked();
        leftDownCellNode.setUnLocked();
        // 离开cellNode解锁使得 nowAGV = null， 再次解锁允许 nowAGV 为 null
//        LOG.info(nowAGV.getID()+"号车 解锁旋转区域：("+leftUpCellNode.getAddressCodeID()+","+rightUpCellNode.getAddressCodeID()+","+rightDownCellNode.getAddressCodeID()+","+leftDownCellNode.getAddressCodeID()+")");
        nowAGV=null;
    }
    /*
     判断此旋转区是否被锁定
     */
    public boolean isRotateAreaLocked(KivaAGV agv) {
        if(agv.equals(nowAGV)) {
             return false;
        }else {
            return true;
        }
    }
 


    /**
     * @return the podID
     */
    public long getWorkstationID() {
        return workstationID;
    }

    /**
     * @param workstationID the podID to set
     */
    public void setWorkstationID(long workstationID) {
        this.workstationID = workstationID;
    }

    /**
     * @return the leftUpCellNode
     */
    public CellNode getLeftUpCellNode() {
        return leftUpCellNode;
    }

    /**
     * @param leftUpCellNode the leftUpCellNode to set
     */
    public void setLeftUpCellNode(CellNode leftUpCellNode) {
        this.leftUpCellNode = leftUpCellNode;
    }

    /**
     * @return the rightUpCellNode
     */
    public CellNode getRightUpCellNode() {
        return rightUpCellNode;
    }

    /**
     * @param rightUpCellNode the rightUpCellNode to set
     */
    public void setRightUpCellNode(CellNode rightUpCellNode) {
        this.rightUpCellNode = rightUpCellNode;
    }

    /**
     * @return the rightDownCellNode
     */
    public CellNode getRightDownCellNode() {
        return rightDownCellNode;
    }

    /**
     * @param rightDownCellNode the rightDownCellNode to set
     */
    public void setRightDownCellNode(CellNode rightDownCellNode) {
        this.rightDownCellNode = rightDownCellNode;
    }

    /**
     * @return the leftDownCellNode
     */
    public CellNode getLeftDownCellNode() {
        return leftDownCellNode;
    }

    /**
     * @param leftDownCellNode the leftDownCellNode to set
     */
    public void setLeftDownCellNode(CellNode leftDownCellNode) {
        this.leftDownCellNode = leftDownCellNode;
    }
    
    public String toString() {
        return "leftUpCellNode="+getLeftUpCellNode()+",rightUpCellNode="+getRightUpCellNode()+",rightDownCellNode="+getRightDownCellNode()+",leftDownCellNode="+getLeftDownCellNode();
    }

}
