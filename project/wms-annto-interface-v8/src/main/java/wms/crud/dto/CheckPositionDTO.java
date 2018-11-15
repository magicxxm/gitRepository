package wms.crud.dto;

import java.io.Serializable;

/**
 * Created by 123 on 2017/8/13.
 */
public class CheckPositionDTO implements Serializable {

    //"companyCode": "货主编码，string (50)，必填",
    private String companyCode;

    //"locationCode": "库位，string (20)，必填",
    private String locationCode;

    //"itemCode": "商品编码，string (20)，必填",
    private String itemCode;

    //"itemName": "商品名称，string (200)，必填",
    private String itemName;

    //"inventoryType": "库存类型（ZP：良品、正品、CP：不良品、次品），string (2)，必填",
    private String inventorySts;

    //"systemQTY": "系统数量，int，必填",
    private int systemQTY;

    //"countedQTY": "实盘数量，int，必填",
    /* private int countedQTY;

   //"countedBy": "盘点人，string (50)，必填",
    private String countedBy;

    //"countedAt": "盘点时间，datetime ",
    private String countedAt;

    //"completedAt": "完成时间，datetime ",
    private String completedAt;

    //"adjustQTY": "调整数量，int，必填",
    private int adjustQTY;*/

    private ExtendFieldsDTO extendFields;

    public CheckPositionDTO() {
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
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

    public String getInventorySts() {
        return inventorySts;
    }

    public void setInventorySts(String inventorySts) {
        this.inventorySts = inventorySts;
    }

    public int getSystemQTY() {
        return systemQTY;
    }

    public void setSystemQTY(int systemQTY) {
        this.systemQTY = systemQTY;
    }

    /*public int getCountedQTY() {
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

    public String getCountedAt() {
        return countedAt;
    }

    public void setCountedAt(String countedAt) {
        this.countedAt = countedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public int getAdjustQTY() {
        return adjustQTY;
    }

    public void setAdjustQTY(int adjustQTY) {
        this.adjustQTY = adjustQTY;
    }*/

    public ExtendFieldsDTO getExtendFields() {
        return extendFields;
    }

    public void setExtendFields(ExtendFieldsDTO extendFields) {
        this.extendFields = extendFields;
    }
}
