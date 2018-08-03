package com.mushiny.wms.masterdata.obbasics.business.order.util.boxpacker;

import java.util.ArrayList;

public class Level extends ArrayList<Item> {

    public int getHeight() {
        int height = 0;

        for (Item box : this) {
            if (box.getHeight() > height) {
                height = box.getHeight();
            }
        }

        return height;
    }
}
