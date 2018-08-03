package com.mushiny.wcs.application.domain.enums;

/**
 * Created by Administrator on 2018/6/7.
 */
public enum ChargeType {
    MUSHINY_2(1),
    MUSHINY_3(3),
    MEIDI(2);
    private final int type;
     ChargeType(int ty)
    {
        this.type=ty;
    }

    public int getType() {
        return type;
    }
}
