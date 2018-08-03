package com.mushiny.wms.report.query.dto.picked;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

public class PickAmount implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal picked;//已拣数量

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal notPicked;//未拣数量

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal total;//总数量

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal pending = BigDecimal.ZERO;//

    public PickAmount() {
    }

    public PickAmount(BigDecimal total) {
        this.total = total;
    }

    public PickAmount(BigDecimal picked,
                      BigDecimal notPicked) {
        this.picked = picked;
        this.notPicked = notPicked;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPicked() {
        return picked;
    }

    public void setPicked(BigDecimal picked) {
        this.picked = picked;
    }

    public BigDecimal getNotPicked() {
        return notPicked;
    }

    public void setNotPicked(BigDecimal notPicked) {
        this.notPicked = notPicked;
    }

    public BigDecimal getPending() {
        return pending;
    }

    public void setPending(BigDecimal pending) {
        this.pending = pending;
    }

    @Override
    public String toString() {
        return "PickAmount{" +
                "picked=" + picked +
                ", notPicked=" + notPicked +
                ", pending=" + pending +
                ", total=" + total +
                '}';
    }
}
