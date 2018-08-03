package com.mushiny.wms.tot.ppr.query.enums;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/7/28.
 */
public enum DateType {
    DAY(0),MONTH(1),WEEK(2),DATETIME(3);
    private int value;

    private DateType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
