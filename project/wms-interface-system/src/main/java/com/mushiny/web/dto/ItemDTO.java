package com.mushiny.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/2/1.
 */
public class ItemDTO implements Serializable {

   private List<ItemDataPosition> positions = new ArrayList<>();

    public List<ItemDataPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ItemDataPosition> positions) {
        this.positions = positions;
    }
}
