package com.mushiny.wcs.application.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DrivePodScore implements Serializable {
    private static final long serialVersionUID = 1L;

    private String podId;

    private String driveId;

    private BigDecimal score;

    private String tripId;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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
