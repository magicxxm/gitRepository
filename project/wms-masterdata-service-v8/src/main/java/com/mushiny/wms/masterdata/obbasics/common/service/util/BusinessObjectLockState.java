package com.mushiny.wms.masterdata.obbasics.common.service.util;

public enum BusinessObjectLockState implements BusinessObjectLock {

    NOT_LOCKED(0),
    GENERAL(1),
    GOING_TO_DELETE(2);

    int lock;

    BusinessObjectLockState(int lock) {
        this.lock = lock;
    }

    public int getLock() {
        return lock;
    }

    public static BusinessObjectLockState resolve(int lock) {
        switch (lock) {
            case 0:
                return NOT_LOCKED;
            case 1:
                return GENERAL;
            case 2:
                return GOING_TO_DELETE;
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
