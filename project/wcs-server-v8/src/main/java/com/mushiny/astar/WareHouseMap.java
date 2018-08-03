package com.mushiny.astar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tank.li on 2017/7/4.
 */
public class WareHouseMap implements java.io.Serializable{
    //地图ID 可能分多个地图
    private int id;
    //地图名字
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //基本地图给空车跑路用 临时锁格、小车故障、格子不可用等
    private Map<NodeKey,Node> basicMap = new HashMap<>();
    //工作地图状态随时会变化，驮货车的路线(Cost值不同)、临时锁格、小车故障、格子不可用等也要考虑
    private Map<NodeKey,Node> workMap = new HashMap<>();

    public Node getBasicNode(NodeKey nodeId){
        return basicMap.get(nodeId);
    }
    public Node getWorkNode(NodeKey nodeId){
        return workMap.get(nodeId);
    }

    public void putBasicNode(NodeKey nodeId,Node node){
         basicMap.put(nodeId,node);
    }

    public void putWorkNode(NodeKey nodeId,Node node){
        workMap.put(nodeId,node);
    }

    public List<Node> getBasicPath(NodeKey nodeKeyA, NodeKey nodeKeyB) {
         Node nodeA = basicMap.get(nodeKeyA);
        return null;
    }
}
