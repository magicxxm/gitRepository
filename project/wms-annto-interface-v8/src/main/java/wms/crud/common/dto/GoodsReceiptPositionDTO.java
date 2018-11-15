package wms.crud.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import wms.common.crud.dto.BaseClientAssignedDTO;
import wms.domain.GoodsReceiptPosition;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class GoodsReceiptPositionDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    private BigDecimal amount;

    private String itemData;

    private String lot;

    @NotNull
    private String stationId;

    @NotNull
    private String receiptType;

    private String state;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiveToStockUnitId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiveToLocation;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String operatorId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String goodsReceiptId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiveToUnitLoad;

//    private StockUnitDTO stockUnit;
//
//    private StorageLocationDTO storageLocation;
//
//    private UnitLoadDTO unitLoad;
//
//    private ReceivingProcessContainerDTO processContainer;
//
//    private UserDTO operator;

    private String lineNo;

    private String itemCode;

    private String itemName;

    private int totalQty;

    private int openQty;

    private String inventorySts;

    private String unit;

    private String batch;

    private String csLot;

    private String companyCode;

    private GoodsReceiptDTO goodsReceipt;

    public GoodsReceiptPositionDTO() {
    }

    public GoodsReceiptPositionDTO(GoodsReceiptPosition entity) {
        super(entity);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReceiveToStockUnitId() {
        return receiveToStockUnitId;
    }

    public void setReceiveToStockUnitId(String receiveToStockUnitId) {
        this.receiveToStockUnitId = receiveToStockUnitId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getGoodsReceiptId() {
        return goodsReceiptId;
    }

    public void setGoodsReceiptId(String goodsReceiptId) {
        this.goodsReceiptId = goodsReceiptId;
    }

//    public StockUnitDTO getStockUnit() {
//        return stockUnit;
//    }
//
//    public void setStockUnit(StockUnitDTO stockUnit) {
//        this.stockUnit = stockUnit;
//    }
//
//    public UserDTO getOperator() {
//        return operator;
//    }
//
//    public void setOperator(UserDTO operator) {
//        this.operator = operator;
//    }
//
//    public GoodsReceiptDTO getGoodsReceipt() {
//        return goodsReceipt;
//    }
//
//    public void setGoodsReceipt(GoodsReceiptDTO goodsReceipt) {
//        this.goodsReceipt = goodsReceipt;
//    }
//
//    public ReceivingProcessContainerDTO getProcessContainer() {
//        return processContainer;
//    }
//
//    public void setProcessContainer(ReceivingProcessContainerDTO processContainer) {
//        this.processContainer = processContainer;
//    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

//    public StorageLocationDTO getStorageLocation() {
//        return storageLocation;
//    }
//
//    public void setStorageLocation(StorageLocationDTO storageLocation) {
//        this.storageLocation = storageLocation;
//    }
//
//    public UnitLoadDTO getUnitLoad() {
//        return unitLoad;
//    }
//
//    public void setUnitLoad(UnitLoadDTO unitLoad) {
//        this.unitLoad = unitLoad;
//    }

    public String getReceiveToLocation() {
        return receiveToLocation;
    }

    public void setReceiveToLocation(String receiveToLocation) {
        this.receiveToLocation = receiveToLocation;
    }

    public String getReceiveToUnitLoad() {
        return receiveToUnitLoad;
    }

    public void setReceiveToUnitLoad(String receiveToUnitLoad) {
        this.receiveToUnitLoad = receiveToUnitLoad;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public int getOpenQty() {
        return openQty;
    }

    public void setOpenQty(int openQty) {
        this.openQty = openQty;
    }

    public String getInventorySts() {
        return inventorySts;
    }

    public void setInventorySts(String inventorySts) {
        this.inventorySts = inventorySts;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getCsLot() {
        return csLot;
    }

    public void setCsLot(String csLot) {
        this.csLot = csLot;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public GoodsReceiptDTO getGoodsReceipt() {
        return goodsReceipt;
    }

    public void setGoodsReceipt(GoodsReceiptDTO goodsReceipt) {
        this.goodsReceipt = goodsReceipt;
    }
}
