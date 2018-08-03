package com.mushiny.beans;

import java.util.Set;

/**
 * Created by Tank.li on 2017/6/25.
 */
public class PodCell implements java.io.Serializable {
    private String cellId;
    private String podPositionId;
    private String podCellName;
    private Set<Item> items;

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getPodPositionId() {
        return podPositionId;
    }

    public void setPodPositionId(String podPositionId) {
        this.podPositionId = podPositionId;
    }

    public String getPodCellName() {
        return podCellName;
    }

    public void setPodCellName(String podCellName) {
        this.podCellName = podCellName;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
