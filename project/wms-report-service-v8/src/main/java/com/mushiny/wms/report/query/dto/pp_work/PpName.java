package com.mushiny.wms.report.query.dto.pp_work;

import java.io.Serializable;
import java.math.BigDecimal;

public class PpName implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private BigDecimal amount = BigDecimal.ZERO;

    public PpName(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
    }

    public PpName() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PpName{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
