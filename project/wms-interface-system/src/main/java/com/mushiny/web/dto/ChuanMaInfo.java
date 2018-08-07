package com.mushiny.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/2/7.
 */
public class ChuanMaInfo implements Serializable {

    private List<ChuanMaInfoPosition> positions = new ArrayList<>();

    public List<ChuanMaInfoPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ChuanMaInfoPosition> positions) {
        this.positions = positions;
    }
}
