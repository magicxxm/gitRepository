package com.mingchun.mu.mushiny.kiva.pod;

import com.mushiny.kiva.map.CellNode;

/**
 * Created by Laptop-6 on 2017/10/17.
 */
public class Pod implements IPod {
    private long podCodeID;
    // 货架码 -- 用于小车举升时，含有货架码的地址码不能锁格下发
    private CellNode cellNode;
    private int podWeight; // pod重量
    public Pod(long podCodeID, CellNode cellNode) {
        this.podCodeID = podCodeID;
        this.cellNode = cellNode;
    }
    public long getPodCodeID() {
        return podCodeID;
    }
    public void setPodCodeID(long podCodeID) {
        this.podCodeID = podCodeID;
    }
    public CellNode getCellNode() {
        return cellNode;
    }
    public void setCellNode(CellNode cellNode) {
        this.cellNode = cellNode;
    }
    public int getPodWeight() {
        return podWeight;
    }
    public void setPodWeight(int podWeight) {
        this.podWeight = podWeight;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(!(obj instanceof IPod)){
            return false;
        }
        IPod pod = (IPod) obj;
        if(this.podCodeID == pod.getPodCodeID()){
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Pod(");
        stringBuilder.append("id=");
        stringBuilder.append(this.podCodeID);
        stringBuilder.append(",");
        stringBuilder.append(" addressCodeID=");
        if(this.cellNode != null){
            stringBuilder.append(this.cellNode.getAddressCodeID());
        }else {
            stringBuilder.append("null");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
