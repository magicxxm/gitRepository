package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequestPosition;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AdviceRequestPositionDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private int positionNo = 0;

    private BigDecimal notifiedAmount;

    private BigDecimal receiptAmount;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String itemDataId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lotId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String adviceId;

    private ItemDataDTO itemData;

//    private LotDTO lot;

    private AdviceRequestDTO adviceRequest;

    public AdviceRequestPositionDTO() {
    }

    public AdviceRequestPositionDTO(AdviceRequestPosition entity) {
        super(entity);
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public BigDecimal getNotifiedAmount() {
        return notifiedAmount;
    }

    public void setNotifiedAmount(BigDecimal notifiedAmount) {
        this.notifiedAmount = notifiedAmount;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(String adviceId) {
        this.adviceId = adviceId;
    }

    public AdviceRequestDTO getAdviceRequest() {
        return adviceRequest;
    }

    public void setAdviceRequest(AdviceRequestDTO adviceRequest) {
        this.adviceRequest = adviceRequest;
    }

    public ItemDataDTO getItemData() {
        return itemData;
    }

    public void setItemData(ItemDataDTO itemData) {
        this.itemData = itemData;
    }

//    public LotDTO getLot() {
//        return lot;
//    }
//
//    public void setLot(LotDTO lot) {
//        this.lot = lot;
//    }
}
