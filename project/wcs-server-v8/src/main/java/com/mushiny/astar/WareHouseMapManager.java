package com.mushiny.astar;

import java.util.List;

/**
 * Created by Tank.li on 2017/7/4.
 */
public class WareHouseMapManager {
    private WareHouseMap wareHouseMap;

    public WareHouseMap getWareHouseMap() {
        return wareHouseMap;
    }

    public void setWareHouseMap(WareHouseMap wareHouseMap) {
        this.wareHouseMap = wareHouseMap;
    }

    public WareHouseMapManager(WareHouseMap wareHouseMap) {
        this.wareHouseMap = wareHouseMap;
    }

    /**
     * 获取基本路径 给空车使用
     * @param x1 起点的x轴
     * @param y1 起点的y轴
     * @param x2 终点的x轴
     * @param y2 终点的y轴
     * @return
     */
    public List<Node> getBasicPath(int x1,int y1, int x2,int y2){
        NodeKey nodeKey1 = new NodeKey(x1,y1);
        NodeKey nodeKey2 = new NodeKey(x2,y2);
        return this.getWareHouseMap().getBasicPath(nodeKey1,nodeKey2);
    }
}
