package com.mushiny.web.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/2/23.
 */
public class SkuNoDTO {

    private List<SkuNoPosition> positions = new ArrayList<>();

    public List<SkuNoPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<SkuNoPosition> positions) {
        this.positions = positions;
    }
}
