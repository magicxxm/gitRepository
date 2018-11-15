package wms.business.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by 123 on 2017/8/17.
 */
public class ReceiptConfirmItemsDTO implements Serializable{

        //"lineNo": "商品行号，string (20)，必填",
    private String lineNo;

    //"itemCode": "商品编码，string (20)，必填",
    private String itemCode;

    //"itemName": "商品名称，string (200)，必填",
    private String itemName;

    //"quantity": "实收数量，int，必填",
    private int quantity;

    //"manufactureDate": "生产日期，date",
    private String manufactureDate;

    //"expirationDate": "失效日期，date",
    private String expirationDate;

    //"agingDate": "入库日期，date",
    private String agingDate;

    //序列号数组,每个序列号用逗号隔开
    private String snCode;

    //"inventorySts": "库存类型（ZP：良品、正品、CP：不良品、次品），string (2)，必填默认 ZP",
    private String inventorySts;

    //"unit": "商品单位，string(10)，必填 默认EA",
    private String unit;

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

    public String getAgingDate() {
        return agingDate;
    }

    public void setAgingDate(String agingDate) {
        this.agingDate = agingDate;
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

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }
}
