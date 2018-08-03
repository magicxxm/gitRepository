package com.mushiny.wms.report.query.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class WorkFlowDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime exsdTime;

    private WorkFlowCutlineDTO cutline;

    public WorkFlowDTO() {
    }


    public WorkFlowDTO(LocalDateTime exsdTime, WorkFlowCutlineDTO cutline) {
        this.exsdTime = exsdTime;
        this.cutline = cutline;
    }

    public WorkFlowDTO(LocalDateTime exsdTime) {
        this.exsdTime = exsdTime;
    }

    public LocalDateTime getExsdTime() {
        return exsdTime;
    }

    public void setExsdTime(LocalDateTime exsdTime) {
        this.exsdTime = exsdTime;
    }

    public WorkFlowCutlineDTO getCutline() {
        return cutline;
    }

    public void setCutline(WorkFlowCutlineDTO cutline) {
        this.cutline = cutline;
    }

}