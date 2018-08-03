package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "MD_SABC_RULE")
public class SabcRule extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "SKU_TYPE_NAME", nullable = false)
    private String skuTypeName;
    @Column(name = "FROM_NO", nullable = false)
    private BigDecimal fromNo;
    @Column(name = "TO_NO", nullable = false)
    private BigDecimal toNo;
    @Column(name = "SALES_PROPORTION")
    private BigDecimal salesPro;
    @Column(name = "MAX_DOC", nullable = false)
    private BigDecimal maxDoc;
    @Column(name = "REPLEN_DOC", nullable = false)
    private BigDecimal replenDoc;

    @Column(name = "SAFELY_DOC", nullable = false)
    private BigDecimal safelyDoc;


    public BigDecimal getFromNo() {
        return fromNo;
    }

    public void setFromNo(BigDecimal fromNo) {
        this.fromNo = fromNo;
    }

    public BigDecimal getToNo() {
        return toNo;
    }

    public void setToNo(BigDecimal toNo) {
        this.toNo = toNo;
    }

    public BigDecimal getMaxDoc() {
        return maxDoc;
    }

    public void setMaxDoc(BigDecimal maxDoc) {
        this.maxDoc = maxDoc;
    }

    public BigDecimal getReplenDoc() {
        return replenDoc;
    }

    public void setReplenDoc(BigDecimal replenDoc) {
        this.replenDoc = replenDoc;
    }

    public BigDecimal getSafelyDoc() {
        return safelyDoc;
    }

    public void setSafelyDoc(BigDecimal safelyDoc) {
        this.safelyDoc = safelyDoc;
    }

    public String getSkuTypeName() {
        return skuTypeName;
    }

    public void setSkuTypeName(String skuTypeName) {
        this.skuTypeName = skuTypeName;
    }

    public BigDecimal getSalesPro() {
        return salesPro;
    }

    public void setSalesPro(BigDecimal salesPro) {
        this.salesPro = salesPro;
    }
}
