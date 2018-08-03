package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceipt;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationDTO;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GoodsReceiptDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String grNo;
    private int size;
    private String deliveryNote;

    private LocalDateTime receiptDate;

    private String receiptState;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String inboundLocationId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String relatedAdviceId;

    private StorageLocationDTO storageLocation;

    private UserDTO operator;

    private AdviceRequestDTO relatedAdvice;

    private List<GoodsReceiptPositionDTO> positions = new ArrayList<>();

    public GoodsReceiptDTO() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public GoodsReceiptDTO(GoodsReceipt entity) {
        super(entity);
    }

    public String getGrNo() {
        return grNo;
    }

    public void setGrNo(String grNo) {
        this.grNo = grNo;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDateTime receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getReceiptState() {
        return receiptState;
    }

    public void setReceiptState(String receiptState) {
        this.receiptState = receiptState;
    }

    public String getInboundLocationId() {
        return inboundLocationId;
    }

    public void setInboundLocationId(String inboundLocationId) {
        this.inboundLocationId = inboundLocationId;
    }

    public String getRelatedAdviceId() {
        return relatedAdviceId;
    }

    public void setRelatedAdviceId(String relatedAdviceId) {
        this.relatedAdviceId = relatedAdviceId;
    }

    public StorageLocationDTO getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocationDTO storageLocation) {
        this.storageLocation = storageLocation;
    }

    public UserDTO getOperator() {
        return operator;
    }

    public void setOperator(UserDTO operator) {
        this.operator = operator;
    }

    public AdviceRequestDTO getRelatedAdvice() {
        return relatedAdvice;
    }

    public void setRelatedAdvice(AdviceRequestDTO relatedAdvice) {
        this.relatedAdvice = relatedAdvice;
    }

    public List<GoodsReceiptPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<GoodsReceiptPositionDTO> positions) {
        this.positions = positions;
    }
}
