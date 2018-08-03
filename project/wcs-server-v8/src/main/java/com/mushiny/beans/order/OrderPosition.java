package com.mushiny.beans.order;

import com.mushiny.beans.BaseObject;

/**
 * Created by Tank.li on 2017/9/6.
 */
public class OrderPosition extends BaseObject {
    private String orderPositionId;

    private String podId;

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    private int index;
    private String useFace;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUseFace() {
        return useFace;
    }

    public void setUseFace(String useFace) {
        this.useFace = useFace;
    }

    public String getOrderPositionId() {
        return orderPositionId;
    }

    public void setOrderPositionId(String orderPositionId) {
        this.orderPositionId = orderPositionId;
    }

    @Override
    public Object getId() {
        return this.orderPositionId;
    }

    public static final String TABLE = "RCS_TRIPPOSITION";

    @Override
    public String getTable() {
        return TABLE;
    }

    public static final String IDNAME = "ID";

    @Override
    public String getIdName() {
        return IDNAME;
    }

    @Override
    public String toString() {
        return "OrderPosition{" +
                "orderPositionId='" + orderPositionId + '\'' +
                ", podId='" + podId + '\'' +
                ", index=" + index +
                ", useFace='" + useFace + '\'' +
                '}';
    }
}
