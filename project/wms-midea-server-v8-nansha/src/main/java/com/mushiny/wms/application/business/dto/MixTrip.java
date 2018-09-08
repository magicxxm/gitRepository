package com.mushiny.wms.application.business.dto;

import com.mushiny.wms.application.domain.Trip;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/18.
 */
public class MixTrip {
    private Trip preTrip;
    private Trip currentTrip;
    private String outSizeAddrcoed;

    public String getOutSizeAddrcoed() {
        return outSizeAddrcoed;
    }

    public void setOutSizeAddrcoed(String outSizeAddrcoed) {
        this.outSizeAddrcoed = outSizeAddrcoed;
    }

    public Trip getPreTrip() {
        return preTrip;
    }

    public void setPreTrip(Trip preTrip) {
        this.preTrip = preTrip;
    }

    public Trip getCurrentTrip() {
        return currentTrip;
    }

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MixTrip)) return false;

        MixTrip mixTrip = (MixTrip) o;

        return currentTrip != null ? currentTrip.getId().equals(mixTrip.currentTrip.getId()) : mixTrip.currentTrip == null;
    }

    @Override
    public int hashCode() {
        int result = preTrip != null ? preTrip.getId().hashCode() : 0;
        result = 31 * result + (currentTrip != null ? currentTrip.getId().hashCode() : 0);
        return result;
    }
}
