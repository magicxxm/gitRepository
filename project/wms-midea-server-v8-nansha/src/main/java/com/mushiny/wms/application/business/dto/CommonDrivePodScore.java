package com.mushiny.wms.application.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CommonDrivePodScore implements Serializable {
    private static final long serialVersionUID = 1L;

    private String podId;

    private String driveId;

    private BigDecimal score;

    private MixTrip mixTrip;

    public MixTrip getMixTrip() {
        return mixTrip;
    }

    public void setMixTrip(MixTrip mixTrip) {
        this.mixTrip = mixTrip;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
