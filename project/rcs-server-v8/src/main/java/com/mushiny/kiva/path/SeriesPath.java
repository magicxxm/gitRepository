/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.global.PathConfig;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.UUID;

/**
 * 路径串 内部规定：第一个必须是起始点
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class SeriesPath implements Comparable<SeriesPath> {
    
    static Logger LOG = LoggerFactory.getLogger(SeriesPath.class.getName());
    private String uuid;
    private int priority = PathConfig.PATH_PRIORITY_COMMON_DEFAULT;//默认优先级
    //--private LinkedList<CellNode> pathList;
    private LinkedList<CellNode> pathList;


    // mingchun.mu@mushiny.com   -- 路径中如果有举升点， 必须等待podID返回后， 方能执行下一段路径
    private CellNode upCellNode; // 是否有举升点（null表示没有）
    private int upPodID; // 需要举升的podID
    private CellNode downCellNode; // 是否有下降点（null表示没有）
    // mingchun.mu@mushiny.com   ------------------------------



    public SeriesPath() {
        this.uuid = UUID.randomUUID().toString();
        pathList = new LinkedList();
    }
    
    public SeriesPath(String uuid) {
        this.uuid = uuid;
        pathList = new LinkedList();
    }
    
    public boolean equals(SeriesPath sp) {
        if (this.getUuid().equals(sp.getUuid())) {
            return true;
        } else {
            return false;
        }
    }
    
    public void addPathCell(CellNode cellNode) {
        getPathList().addLast(cellNode);
    }
    
    public void removePathCell(CellNode cellNode) {
        getPathList().remove(cellNode);
    }
    
    public void setCellPathList(LinkedList<CellNode> path) {
        this.getPathList().clear();
        for (CellNode cell : path) {
            this.getPathList().add(cell);
        }
    }
    
    public CellNode getCellNodeByIndex(int index) {
        if (pathList.isEmpty()) {
            return null;
        }
        if (index >= getCellListSize()) {
            return null;
        }
        return pathList.get(index);
    }

    //得到CELL数量
    public int getCellListSize() {
        return getPathList().size();
    }

    //得到路径长度
    public int getPathLength() {
        int len = 0;
        for (CellNode cellNode : getPathList()) {
            LinkedList<RobotAction> actionList = cellNode.getRobotActionList(this);
            len += actionList.size();
        }
        return len;
    }

    //路径串比较器
    @Override
    public int compareTo(SeriesPath otherSeriesPath) {
        return priority - otherSeriesPath.getPriority();
    }

    //把路径转换为字节协议
    public byte[] toBytes() {
        byte[] pathBytes = new byte[getPathLength() * 8];
        int lastCellBytesLength = 0;
        for (CellNode cell : getPathList()) {
            byte[] cellBytes = cell.toPathActionBytes(this);
            System.arraycopy(cellBytes, 0, pathBytes, lastCellBytesLength, cellBytes.length);
            lastCellBytesLength += cellBytes.length;
        }
        return pathBytes;
    }

    /**
     * @return the pathList
     */
    public LinkedList<CellNode> getPathList() {
        return pathList;
    }

    /**
     * @param pathList the pathList to set
     */
    public void setPathList(LinkedList<CellNode> pathList) {
        this.pathList = pathList;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public String toString() {
        StringBuilder builder1 = new StringBuilder();
        builder1.append("[");
        for (CellNode node : getPathList()) {
            builder1.append(node.getAddressCodeID());
            builder1.append("[");

//            LOG.info("addressCodeID("+node.getAddressCodeID()+")  RobotActionList="+node.getRobotActionList(this));
            if(node.getRobotActionList(this) != null)
            for (RobotAction robotAction : node.getRobotActionList(this)) {
                if (robotAction != null) {
                    builder1.append(robotAction.toString() + "|");
                } else {
                    LOG.error("===========================路径串错误！！！动作码为空！！！");
                }
            }
            builder1.append("],");
        }
        if(getUpCellNode() != null){
            builder1.append("upAddressCodeID="+getUpCellNode().getAddressCodeID());
        }
        if(getDownCellNode() != null){
            builder1.append("downAddressCodeID="+getDownCellNode().getAddressCodeID());
        }
        builder1.append("]");
        return builder1.toString();
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }
    public CellNode getUpCellNode() {
        return upCellNode;
    }
    public void setUpCellNode(CellNode upCellNode) {
        this.upCellNode = upCellNode;
    }
    public int getUpPodID() {
        return upPodID;
    }
    public void setUpPodID(int upPodID) {
        this.upPodID = upPodID;
    }
    public CellNode getDownCellNode() {
        return downCellNode;
    }
    public void setDownCellNode(CellNode downCellNode) {
        this.downCellNode = downCellNode;
    }
}
