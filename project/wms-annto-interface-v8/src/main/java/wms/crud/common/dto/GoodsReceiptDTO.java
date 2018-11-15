package wms.crud.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import wms.common.crud.dto.BaseClientAssignedDTO;
import wms.domain.GoodsReceipt;

import javax.validation.constraints.NotNull;
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

//    private StorageLocationDTO storageLocation;
//
//    private UserDTO operator;

    private AdviceRequestDTO relatedAdvice;

    private String code;

    private String warehouseCode;

    private String companyCode;

    private String receiptType;

    private String shipFromCode;

    private String shipFromName;

    private String pln;

    private String receiptNote;

    private List<GoodsReceiptPositionDTO> receiptItems = new ArrayList<>();

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

//    public StorageLocationDTO getStorageLocation() {
//        return storageLocation;
//    }
//
//    public void setStorageLocation(StorageLocationDTO storageLocation) {
//        this.storageLocation = storageLocation;
//    }
//
//    public UserDTO getOperator() {
//        return operator;
//    }
//
//    public void setOperator(UserDTO operator) {
//        this.operator = operator;
//    }

    public AdviceRequestDTO getRelatedAdvice() {
        return relatedAdvice;
    }

    public void setRelatedAdvice(AdviceRequestDTO relatedAdvice) {
        this.relatedAdvice = relatedAdvice;
    }

    public List<GoodsReceiptPositionDTO> getPositions() {
        return receiptItems;
    }

    public void setPositions(List<GoodsReceiptPositionDTO> receiptItems) {
        this.receiptItems = receiptItems;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public List<GoodsReceiptPositionDTO> getReceiptItems() {
        return receiptItems;
    }

    public void setReceiptItems(List<GoodsReceiptPositionDTO> receiptItems) {
        this.receiptItems = receiptItems;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getShipFromCode() {
        return shipFromCode;
    }

    public void setShipFromCode(String shipFromCode) {
        this.shipFromCode = shipFromCode;
    }

    public String getShipFromName() {
        return shipFromName;
    }

    public void setShipFromName(String shipFromName) {
        this.shipFromName = shipFromName;
    }

    public String getPln() {
        return pln;
    }

    public void setPln(String pln) {
        this.pln = pln;
    }

    public String getReceiptNote() {
        return receiptNote;
    }

    public void setReceiptNote(String receiptNote) {
        this.receiptNote = receiptNote;
    }
}
