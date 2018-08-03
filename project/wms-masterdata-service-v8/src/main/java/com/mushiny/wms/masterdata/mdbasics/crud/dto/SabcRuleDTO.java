package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.SabcRule;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SabcRuleDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;
    @NotNull
    private String skuTypeName;
    @NotNull
    private BigDecimal fromNo;
    @NotNull
    private BigDecimal toNo;

    private BigDecimal salesPro=BigDecimal.ZERO;
    @NotNull
    private BigDecimal maxDoc;
    @NotNull
    private BigDecimal replenDoc;
    @NotNull
    private BigDecimal safelyDoc;

    public String getSkuTypeName() {
        return skuTypeName;
    }

    public void setSkuTypeName(String skuTypeName) {
        this.skuTypeName = skuTypeName;
    }

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

    public SabcRuleDTO() {
    }

    public SabcRuleDTO(SabcRule entity) {
        super(entity);
    }

    public BigDecimal getSalesPro() {
        return salesPro;
    }

    public void setSalesPro(BigDecimal salesPro) {
        this.salesPro = salesPro;
    }
}
