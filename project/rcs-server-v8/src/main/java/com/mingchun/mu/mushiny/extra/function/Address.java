package com.mingchun.mu.mushiny.extra.function;

/**
 * Created by Laptop-6 on 2017/12/15.
 */
public class Address {
    private long addressCodeId;
    private boolean isLost;

    public Address() {
    }

    public Address(int addressCodeId, boolean isLost) {
        this.addressCodeId = addressCodeId;
        this.isLost = isLost;
    }

    public long getAddressCodeId() {
        return addressCodeId;
    }

    public void setAddressCodeId(long addressCodeId) {
        this.addressCodeId = addressCodeId;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }
}
