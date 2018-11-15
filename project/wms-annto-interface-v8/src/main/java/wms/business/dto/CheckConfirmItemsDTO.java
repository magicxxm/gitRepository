package wms.business.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by 123 on 2017/8/17.
 */
public class CheckConfirmItemsDTO implements Serializable {

    //行号
    private String lineNo;

    //"companyCode": "货主编码，string (50)，必填"
    private String companyCode;

    //"locationCode": "库位，string (20)，必填",
    private String locationCode;

    //        "itemCode": "商品编码，string (20)，必填",
    private String itemCode;

    //"itemName": "商品名称，string (200)",
      private String itemName;

     // "inventorySts": "库存类型，string (20)",
    private String inventorySts;

     //"countedQTY": "实盘数量，int，必填",
    private int countedQty;

    //批次
    /*private String batch;

    //批号
    private String lot;*/

    //生产日期
    private String manufactureDate;

    //失效日期
    private String expirationDate;

    //"countedBy": "盘点人，string (50)，必填",
    private String countedBy;

    //"countedAt": "盘点时间，datetime ",
    private String countedAt;

    //"completedAt": "完成时间，datetime ",
    private String completedAt;

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
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

    public int getCountedQty() {
        return countedQty;
    }

    public void setCountedQty(int countedQty) {
        this.countedQty = countedQty;
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

//    public String getBatch() {
//        return batch;
//    }
//
//    public void setBatch(String batch) {
//        this.batch = batch;
//    }
//
//    public String getLot() {
//        return lot;
//    }
//
//    public void setLot(String lot) {
//        this.lot = lot;
//    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
