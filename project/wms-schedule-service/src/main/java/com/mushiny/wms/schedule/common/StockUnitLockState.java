package com.mushiny.wms.schedule.common;

public enum StockUnitLockState implements BusinessObjectLock{

    PICKED_FOR_GOODSOUT(100),
    REBINED_FOR_GOODSOUT(104),
    UNEXPECTED_NULL(101),
    LOT_EXPIRED(102),
    QUALITY_FAULT(103);

    int lock;

    StockUnitLockState(int lock) {
        this.lock = lock;
    }

    public int getLock() {
        return lock;
    }

    public static StockUnitLockState resolve(int lock) {
        switch (lock) {
            case 100:
                return PICKED_FOR_GOODSOUT;
            case 101:
                return UNEXPECTED_NULL;
            case 102:
                return LOT_EXPIRED;
            case 103:
                return QUALITY_FAULT;
            default:
                throw new IllegalArgumentException();
        }
    }

    public String getMessage() {
        String key = getMessageKey();
        return key;
    }

    public String getMessageKey() {
        return this.name() + "_" + lock;
    }
}
