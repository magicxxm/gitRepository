package com.mushiny.wms.report.query.dto.pp_work;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class WorkPpNameTime implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime exsdTime;

    private BigDecimal amount = BigDecimal.ZERO;

    public WorkPpNameTime() {
    }

    public WorkPpNameTime(LocalDateTime exsdTime, BigDecimal amount) {
        this.exsdTime = exsdTime;
        this.amount = amount;
    }

    public WorkPpNameTime(LocalDateTime exsdTime) {
        this.exsdTime = exsdTime;
    }

    public LocalDateTime getExsdTime() {
        return exsdTime;
    }

    public void setExsdTime(LocalDateTime exsdTime) {
        this.exsdTime = exsdTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PpNameTime{" +
                "exsdTime=" + exsdTime +
                ", amount=" + amount +
                '}';
    }
}
