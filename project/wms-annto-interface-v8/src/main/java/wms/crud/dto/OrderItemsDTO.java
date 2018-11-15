package wms.crud.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 */
public class OrderItemsDTO {

    //"companyCode": "货主编码，string (50)，必填",
    private String companyCode;

    //"lineNo": "商品行号，string (20)，必填",
    private String lineNo;

    //"itemCode": "商品编码，string (20)，必填",
    private String itemCode;

    //"itemName": "商品名称，string (200)，必填",
    private String itemName;

    //采购单价
    private double purchasePrice;

    //计划数量
    private BigDecimal planQty;

    //"quantity": "数量，int，必填",
    private int quantity;

    //"batch": "批次，string (50)",
    private String batch;

    //"lot": "批号，string (50)",
    private String lot;

    //是否套装
    private String kitFlag;

    //备注
    private String remark;

    //"manufactureDate": "生产日期，date",
    private LocalDate manufactureDate;

    //"expirationDate": "失效日期，date",
    private LocalDate expirationDate;

    //"agingDate": "入库日期，date",
    private LocalDate agingDate;

    //"inventorySts": "库存类型（ZP：良品、正品、CP：不良品、次品），string (2)，必填默认 ZP",
    private String inventorySts = "ZP";

    //"inventoryType": "库存类型（ZP：良品、正品、CP：不良品、次品），string (2)，必填",
    private String inventoryType;

    //"lotNo": "批号，string (50)",
    private String lotNo;

    //"unit": "商品单位，string(10)，必填 默认EA",
    private String unit = "EA";

    //"locationCode": "库位，string (20)，必填",
    private String locationCode;

    //"systemQTY": "系统数量，int，必填",
    private int systemQTY;

    //"countedQTY": "实盘数量，int，必填",
    private int countedQTY;

    //"countedBy": "盘点人，string (50)，必填",
    private String countedBy;

    //"countedAt": "盘点时间，datetime ",
    private LocalDateTime countedAt;

    //"completedAt": "完成时间，datetime ",
    private LocalDateTime completedAt;

    //"adjustQTY": "调整数量，int，必填",
    private int adjustQTY;

    //"allocatedQty": "计划数量，int，必填",
    private int allocatedQty;

    //"fromloc": "从库位，string (20)，必填",
    private String fromloc;

    //"toloc": "到库位，string (20)，必填",
    private String toloc;

    //"fromZone": "从区域，string (20)，必填",
    private String fromZone;

    //"toZone": "到区域，string (20)，必填",
    private String toZone;

    //"fromLpn": "从托盘，string (20)，必填",
    private String fromLpn;

    //"toLpn": "到托盘，string (20)，必填",
    private String toLpn;

    private ExtendFieldsDTO extendFieldsDTO;

    public OrderItemsDTO() {
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getAgingDate() {
        return agingDate;
    }

    public void setAgingDate(LocalDate agingDate) {
        this.agingDate = agingDate;
    }

    public String getInventorySts() {
        return inventorySts;
    }

    public void setInventorySts(String inventorySts) {
        this.inventorySts = inventorySts;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public int getSystemQTY() {
        return systemQTY;
    }

    public void setSystemQTY(int systemQTY) {
        this.systemQTY = systemQTY;
    }

    public int getCountedQTY() {
        return countedQTY;
    }

    public void setCountedQTY(int countedQTY) {
        this.countedQTY = countedQTY;
    }

    public String getCountedBy() {
        return countedBy;
    }

    public void setCountedBy(String countedBy) {
        this.countedBy = countedBy;
    }

    public LocalDateTime getCountedAt() {
        return countedAt;
    }

    public void setCountedAt(LocalDateTime countedAt) {
        this.countedAt = countedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public int getAdjustQTY() {
        return adjustQTY;
    }

    public void setAdjustQTY(int adjustQTY) {
        this.adjustQTY = adjustQTY;
    }

    public int getAllocatedQty() {
        return allocatedQty;
    }

    public void setAllocatedQty(int allocatedQty) {
        this.allocatedQty = allocatedQty;
    }

    public String getFromloc() {
        return fromloc;
    }

    public void setFromloc(String fromloc) {
        this.fromloc = fromloc;
    }

    public String getToloc() {
        return toloc;
    }

    public void setToloc(String toloc) {
        this.toloc = toloc;
    }

    public String getFromZone() {
        return fromZone;
    }

    public void setFromZone(String fromZone) {
        this.fromZone = fromZone;
    }

    public String getToZone() {
        return toZone;
    }

    public void setToZone(String toZone) {
        this.toZone = toZone;
    }

    public String getFromLpn() {
        return fromLpn;
    }

    public void setFromLpn(String fromLpn) {
        this.fromLpn = fromLpn;
    }

    public String getToLpn() {
        return toLpn;
    }

    public void setToLpn(String toLpn) {
        this.toLpn = toLpn;
    }

    public ExtendFieldsDTO getExtendFieldsDTO() {
        return extendFieldsDTO;
    }

    public void setExtendFieldsDTO(ExtendFieldsDTO extendFieldsDTO) {
        this.extendFieldsDTO = extendFieldsDTO;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getPlanQty() {
        return planQty;
    }

    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }

    public String getKitFlag() {
        return kitFlag;
    }

    public void setKitFlag(String kitFlag) {
        this.kitFlag = kitFlag;
    }
}
