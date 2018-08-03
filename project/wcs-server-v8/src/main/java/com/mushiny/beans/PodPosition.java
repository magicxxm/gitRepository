package com.mushiny.beans;

import java.util.List;
import java.util.Set;

/**
 * Created by Tank.li on 2017/6/25.
 */
public class PodPosition {
    private String podPositionId;
    private String podPositionName;
    private String position;//四个面A B C D
    private String podId;
    private Set<PodCell> podCells;

    public String getPodPositionId() {
        return podPositionId;
    }

    public void setPodPositionId(String podPositionId) {
        this.podPositionId = podPositionId;
    }

    public String getPodPositionName() {
        return podPositionName;
    }

    public void setPodPositionName(String podPositionName) {
        this.podPositionName = podPositionName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public Set<PodCell> getPodCells() {
        return podCells;
    }

    public void setPodCells(Set<PodCell> podCells) {
        this.podCells = podCells;
    }
}
