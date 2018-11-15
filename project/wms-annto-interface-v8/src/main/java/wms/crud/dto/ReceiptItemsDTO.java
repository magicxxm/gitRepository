package wms.crud.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 */
public class ReceiptItemsDTO {

    //"lineNo": "商品行号，string (20)，必填",
    private String lineNo;

    //"itemCode": "商品编码，string (20)，必填",
    private String itemCode;

    //"itemName": "商品名称，string (200)",
    private String itemName;

    //"totalQty": "总数，int，必填",
    private int totalQty;

    //"openQTY": "应收数量，int，必填 新单时与总数相同",
    private int openQty;

    //"unit": "商品单位，string(10)，必填 默认EA",
    private String unit = "EA";

    //"batck": "批次，string (20)",
//    private String batch;

    //"lot": "批号，string (20)",
//    private String lot;

    //"manufactureDate": "生产日期，date",
    private Date manufactureDate;

    //"expirationDate": "失效日期，date",
    private Date expirationDate;

    //"inventorySts": "库存状态，（ZP：正品、CP：残品、DDC：待调查、DTZ：待调整），string (2)，必填",
    private String inventorySts;

    //"kitFlag": "是否套装【Y：是，N：不是】，string(10)，必填 默认Ｎ",
    private String kitFlag = "N";

    //"remark": "备注，string (500)",
    private String remark;

    private ExtendFieldsDTO extendFields;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getInventorySts() {
        return inventorySts;
    }

    public void setInventorySts(String inventorySts) {
        this.inventorySts = inventorySts;
    }

    public String getKitFlag() {
        return kitFlag;
    }

    public void setKitFlag(String kitFlag) {
        this.kitFlag = kitFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ExtendFieldsDTO getExtendFields() {
        return extendFields;
    }

    public void setExtendFields(ExtendFieldsDTO extendFields) {
        this.extendFields = extendFields;
    }
}
