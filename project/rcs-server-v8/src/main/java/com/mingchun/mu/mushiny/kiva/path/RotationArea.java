package com.mingchun.mu.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.Map;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.astart.Node;
import com.mushiny.rcs.server.KivaAGV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2018/4/27 0027.
 */
public class RotationArea implements IRotationArea {

    private Logger LOG = LoggerFactory.getLogger(RotationArea.class.getName());

    private int id;

    private int workStationAngle; // 工位面向角度
    private long rotationAddressCodeID; // 旋转点地址码
    private CellNode rotationCellNode; // 旋转点，用于旋转角度
    private List<CellNode> rotationAreaCellNodes = new CopyOnWriteArrayList<>(); // 旋转区需要锁住的点集合
    private List<CellNode> rotationAreaOutCellNodes = new CopyOnWriteArrayList<>(); // 从旋转区离开后的第一个点集合
    private Map kivaMap;

    public RotationArea(int id, CellNode rotationCellNode, int workStationAngle) {
        kivaMap = MapManager.getInstance().getMap(); // 地图创建完才创建旋转区
        this.id = id;
        this.rotationCellNode = rotationCellNode;
        this.rotationAddressCodeID = this.rotationCellNode.getAddressCodeID();
        this.workStationAngle = workStationAngle;
        init();
        LOG.info("创建旋转区："+this.toString());
    }
    public RotationArea(int id, long rotationAddressCodeID, int workStationAngle) {
        kivaMap = MapManager.getInstance().getMap();
        this.id = id;
        this.rotationAddressCodeID = rotationAddressCodeID;
        this.rotationCellNode = kivaMap.getMapCellByAddressCodeID(rotationAddressCodeID);
        this.workStationAngle = workStationAngle;
        init();
        LOG.info("创建旋转区："+this.toString());
    }
    private void init(){
        initRotationAreaCellNodes();
        initRotationAreaOutCellNodes();
    }
    private void initRotationAreaCellNodes(){
        if(rotationCellNode == null){
            return;
        }
        rotationAreaCellNodes.add(rotationCellNode);
        if(workStationAngle == 0
                || workStationAngle == 180){ // 工作站面朝0或180度
            CellNode leftNode = (CellNode) rotationCellNode.getLeftNode();
            CellNode rightNode = (CellNode) rotationCellNode.getRightNode();
            if((leftNode != null && leftNode.isWalkable()) && (rightNode != null && rightNode.isWalkable())){
                rotationAreaCellNodes.add(leftNode);
                rotationAreaCellNodes.add(rightNode);
            }
        }
        if(workStationAngle == 90
                || workStationAngle == 270){ // 工作站面朝90或270度
            CellNode upNode = (CellNode) rotationCellNode.getUpNode();
            CellNode downNode = (CellNode) rotationCellNode.getDownNode();
            if((upNode != null && upNode.isWalkable()) && (downNode != null && downNode.isWalkable())){
                rotationAreaCellNodes.add(upNode);
                rotationAreaCellNodes.add(downNode);
            }
        }
    }
    private void initRotationAreaOutCellNodes(){
        if(rotationCellNode == null){
            return;
        }
        switch (workStationAngle){
            case 0:
                CellNode downNode = (CellNode) rotationCellNode.getDownNode();
                if((downNode != null && downNode.isWalkable())){
                    rotationAreaOutCellNodes.add(downNode);
                }
                break;
            case 180:
                CellNode upNode = (CellNode) rotationCellNode.getUpNode();
                if((upNode != null && upNode.isWalkable())){
                    rotationAreaOutCellNodes.add(upNode);
                }
                break;
            case 90:
                CellNode leftNode = (CellNode) rotationCellNode.getLeftNode();
                if((leftNode != null && leftNode.isWalkable())){
                    rotationAreaOutCellNodes.add(leftNode);
                }
                break;
            case 270:
                CellNode rightNode = (CellNode) rotationCellNode.getRightNode();
                if((rightNode != null && rightNode.isWalkable())){
                    rotationAreaOutCellNodes.add(rightNode);
                }
                break;
            default:
                break;
        }
    }
    private void initRotationAreaOutCellNodes_old(){
        if(rotationCellNode == null){
            return;
        }
        for(CellNode temp : rotationAreaCellNodes){
            for(Node temp2 : temp.getSurroudNodes()){
                CellNode outCellNode = (CellNode) temp2;
                if(!isCellNodeInRotationAreaCellNodes(outCellNode) && outCellNode.isWalkable()){
                    rotationAreaOutCellNodes.add(outCellNode);
                }
            }
        }
    }
    public boolean isCellNodeInRotationAreaCellNodes(CellNode cellNode){
        if(cellNode == null){
            return false;
        }
        for(CellNode temp : rotationAreaCellNodes){
            if(temp.getAddressCodeID() == cellNode.getAddressCodeID()){
                return true;
            }
        }
        return false;
    }

    public boolean isCellNodeInRotationAreaOutCellNodes(CellNode cellNode){
        if(cellNode == null){
            return false;
        }
        for(CellNode temp : rotationAreaOutCellNodes){
            if(temp.getAddressCodeID() == cellNode.getAddressCodeID()){
                return true;
            }
        }
        return false;
    }

    public boolean lockRotationArea(KivaAGV agv){
        if(rotationAreaCellNodes == null){
            return false;
        }
        if(rotationAreaCellNodes.size() == 0){
            return false;
        }
        for(CellNode cellNode : rotationAreaCellNodes){
            if(!cellNode.isLocked()){
                cellNode.setLocked(agv);
            }
        }
        return true;
    }
    public boolean unlockRotationArea(KivaAGV agv){
        if(rotationAreaCellNodes == null){
            return false;
        }
        if(rotationAreaCellNodes.size() == 0){
            return false;
        }
        if(isCellNodeInRotationAreaOutCellNodes(kivaMap.getMapCellByAddressCodeID(agv.getCurrentAddressCodeID()))){
            for(CellNode cellNode : rotationAreaCellNodes){
                if(cellNode.getNowLockedAGV() == null){
                    continue;
                }
                if(cellNode.getNowLockedAGV().equals(agv)){
                    cellNode.setUnLocked();
                }
            }
        }
        return true;
    }

    public int getWorkStationAngle() {
        return workStationAngle;
    }

    public void setWorkStationAngle(int workStationAngle) {
        this.workStationAngle = workStationAngle;
    }

    public long getRotationAddressCodeID() {
        return rotationAddressCodeID;
    }

    public void setRotationAddressCodeID(long rotationAddressCodeID) {
        this.rotationAddressCodeID = rotationAddressCodeID;
    }

    public CellNode getRotationCellNode() {
        return rotationCellNode;
    }

    public void setRotationCellNode(CellNode rotationCellNode) {
        this.rotationCellNode = rotationCellNode;
    }

    public List<CellNode> getRotationAreaCellNodes() {
        return rotationAreaCellNodes;
    }


    public List<CellNode> getRotationAreaOutCellNodes() {
        return rotationAreaOutCellNodes;
    }

    public int getId() {
        return id;
    }

    public boolean equals(IRotationArea rotationArea) {
        if(rotationArea == null){
            return false;
        }
        return this.id == rotationArea.getId();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("id:"+this.id);
        stringBuilder.append(",");
        stringBuilder.append("rotationAddressCodeID:"+this.rotationAddressCodeID);
        stringBuilder.append(",");
        stringBuilder.append("workStationAngle:"+this.workStationAngle);
        stringBuilder.append(",");
        stringBuilder.append("rotationAreaCellNodes:("+cellNodeList2AddressCodeIDString(this.rotationAreaCellNodes)+")");
        stringBuilder.append(",");
        stringBuilder.append("rotationAreaOutCellNodes:("+cellNodeList2AddressCodeIDString(this.rotationAreaOutCellNodes)+")");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
    private String cellNodeList2AddressCodeIDString(List<CellNode> cellNodeList){
        if(cellNodeList == null
                || cellNodeList.size() == 0){
            return "";
        }
        StringBuilder temp = new StringBuilder();
        for(CellNode cellNode : cellNodeList){
            if(!temp.toString().trim().equals("")){
                temp.append(",");
            }
            temp.append(cellNode.getAddressCodeID());
        }
        return temp.toString();
    }


}
