package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;

@Entity
@Table(name = "MD_BATTERCONFIG")
public class BatterConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "START_NUMBER", nullable = false)
    private BigDecimal startNumber;

    @Column(name = "END_NUMBER", nullable = false)
    private BigDecimal endNumber;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(BigDecimal startNumber) {
        this.startNumber = startNumber;
    }

    public BigDecimal getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(BigDecimal endNumber) {
        this.endNumber = endNumber;
    }
}
