package com.mushiny.web.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 123 on 2018/3/1.
 */
public class Priority implements Serializable {

    private List<PriorityPosition> positions;

    public List<PriorityPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PriorityPosition> positions) {
        this.positions = positions;
    }
}
